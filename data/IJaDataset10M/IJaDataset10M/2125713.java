package jpcsp.graphics.RE.software;

import jpcsp.Memory;
import jpcsp.graphics.GeCommands;
import jpcsp.memory.FastMemory;
import jpcsp.memory.IMemoryReaderWriter;

/**
 * @author gid15
 *
 */
public class RendererWriter {

    public static IRendererWriter getRendererWriter(int fbAddress, int fbBufferWidth, int fbPixelFormat, int depthAddress, int depthBufferWidth, int depthPixelFormat, boolean needDepthRead, boolean needDepthWrite) {
        Memory mem = Memory.getInstance();
        if (mem instanceof FastMemory) {
            return getFastMemoryRendererWriter((FastMemory) mem, fbAddress, fbBufferWidth, fbPixelFormat, depthAddress, depthBufferWidth, depthPixelFormat, needDepthRead, needDepthWrite);
        }
        return getRendererWriterGeneric(fbAddress, fbBufferWidth, fbPixelFormat, depthAddress, depthBufferWidth, depthPixelFormat, needDepthRead, needDepthWrite);
    }

    private static IRendererWriter getFastMemoryRendererWriter(FastMemory mem, int fbAddress, int fbBufferWidth, int fbPixelFormat, int depthAddress, int depthBufferWidth, int depthPixelFormat, boolean needDepthRead, boolean needDepthWrite) {
        int[] memInt = mem.getAll();
        if (depthPixelFormat == BaseRenderer.depthBufferPixelFormat) {
            switch(fbPixelFormat) {
                case GeCommands.TPSM_PIXEL_STORAGE_MODE_32BIT_ABGR8888:
                    if (needDepthRead) {
                        if (needDepthWrite) {
                            return new RendererWriterInt32(memInt, fbAddress, depthAddress);
                        }
                        return new RendererWriterNoDepthWriteInt32(memInt, fbAddress, depthAddress);
                    }
                    if (needDepthWrite) {
                        return new RendererWriterNoDepthReadInt32(memInt, fbAddress, depthAddress);
                    }
                    return new RendererWriterNoDepthReadWriteInt32(memInt, fbAddress);
            }
        }
        return getRendererWriterGeneric(fbAddress, fbBufferWidth, fbPixelFormat, depthAddress, depthBufferWidth, depthPixelFormat, needDepthRead, needDepthWrite);
    }

    private static IRendererWriter getRendererWriterGeneric(int fbAddress, int fbBufferWidth, int fbPixelFormat, int depthAddress, int depthBufferWidth, int depthPixelFormat, boolean needDepthRead, boolean needDepthWrite) {
        if (!needDepthRead && !needDepthWrite) {
            return new RendererWriterGenericNoDepth(fbAddress, fbBufferWidth, fbPixelFormat);
        }
        return new RendererWriterGeneric(fbAddress, fbBufferWidth, fbPixelFormat, depthAddress, depthBufferWidth, depthPixelFormat);
    }

    private static final class RendererWriterGeneric implements IRendererWriter {

        private final IMemoryReaderWriter fbWriter;

        private final IMemoryReaderWriter depthWriter;

        public RendererWriterGeneric(int fbAddress, int fbBufferWidth, int fbPixelFormat, int depthAddress, int depthBufferWidth, int depthPixelFormat) {
            fbWriter = ImageWriter.getImageWriter(fbAddress, fbBufferWidth, fbBufferWidth, fbPixelFormat);
            depthWriter = ImageWriter.getImageWriter(depthAddress, depthBufferWidth, depthBufferWidth, depthPixelFormat);
        }

        @Override
        public void readCurrent(ColorDepth colorDepth) {
            colorDepth.color = fbWriter.readCurrent();
            colorDepth.depth = depthWriter.readCurrent();
        }

        @Override
        public void writeNext(ColorDepth colorDepth) {
            fbWriter.writeNext(colorDepth.color);
            depthWriter.writeNext(colorDepth.depth);
        }

        @Override
        public void writeNextColor(int color) {
            fbWriter.writeNext(color);
            depthWriter.skip(1);
        }

        @Override
        public void skip(int fbCount, int depthCount) {
            fbWriter.skip(fbCount);
            depthWriter.skip(depthCount);
        }

        @Override
        public void flush() {
            fbWriter.flush();
            depthWriter.flush();
        }
    }

    private static final class RendererWriterGenericNoDepth implements IRendererWriter {

        private final IMemoryReaderWriter fbWriter;

        public RendererWriterGenericNoDepth(int fbAddress, int fbBufferWidth, int fbPixelFormat) {
            fbWriter = ImageWriter.getImageWriter(fbAddress, fbBufferWidth, fbBufferWidth, fbPixelFormat);
        }

        @Override
        public void readCurrent(ColorDepth colorDepth) {
            colorDepth.color = fbWriter.readCurrent();
        }

        @Override
        public void writeNext(ColorDepth colorDepth) {
            fbWriter.writeNext(colorDepth.color);
        }

        @Override
        public void writeNextColor(int color) {
            fbWriter.writeNext(color);
        }

        @Override
        public void skip(int fbCount, int depthCount) {
            fbWriter.skip(fbCount);
        }

        @Override
        public void flush() {
            fbWriter.flush();
        }
    }

    private static final class RendererWriterInt32 implements IRendererWriter {

        private int fbIndex;

        private int depthIndex;

        private int depthOffset;

        private final int[] memInt;

        public RendererWriterInt32(int[] memInt, int fbAddress, int depthAddress) {
            this.memInt = memInt;
            fbIndex = (fbAddress & Memory.addressMask) >> 2;
            depthIndex = (depthAddress & Memory.addressMask) >> 2;
            depthOffset = (depthAddress >> 1) & 1;
        }

        @Override
        public void readCurrent(ColorDepth colorDepth) {
            colorDepth.color = memInt[fbIndex];
            if (depthOffset == 0) {
                colorDepth.depth = memInt[depthIndex] & 0x0000FFFF;
            } else {
                colorDepth.depth = memInt[depthIndex] >>> 16;
            }
        }

        private void next() {
            fbIndex++;
            if (depthOffset == 0) {
                depthOffset = 1;
            } else {
                depthIndex++;
                depthOffset = 0;
            }
        }

        @Override
        public void writeNext(ColorDepth colorDepth) {
            memInt[fbIndex] = colorDepth.color;
            if (depthOffset == 0) {
                memInt[depthIndex] = (memInt[depthIndex] & 0xFFFF0000) | (colorDepth.depth & 0x0000FFFF);
            } else {
                memInt[depthIndex] = (memInt[depthIndex] & 0x0000FFFF) | (colorDepth.depth << 16);
            }
            next();
        }

        @Override
        public void writeNextColor(int color) {
            memInt[fbIndex] = color;
            next();
        }

        @Override
        public void skip(int fbCount, int depthCount) {
            fbIndex += fbCount;
            depthOffset += depthCount;
            depthIndex += depthOffset >> 1;
            depthOffset &= 1;
        }

        @Override
        public void flush() {
        }
    }

    private static final class RendererWriterNoDepthReadInt32 implements IRendererWriter {

        private int fbIndex;

        private int depthIndex;

        private int depthOffset;

        private final int[] memInt;

        public RendererWriterNoDepthReadInt32(int[] memInt, int fbAddress, int depthAddress) {
            this.memInt = memInt;
            fbIndex = (fbAddress & Memory.addressMask) >> 2;
            depthIndex = (depthAddress & Memory.addressMask) >> 2;
            depthOffset = (depthAddress >> 1) & 1;
        }

        @Override
        public void readCurrent(ColorDepth colorDepth) {
            colorDepth.color = memInt[fbIndex];
        }

        private void next() {
            fbIndex++;
            if (depthOffset == 0) {
                depthOffset = 1;
            } else {
                depthIndex++;
                depthOffset = 0;
            }
        }

        @Override
        public void writeNext(ColorDepth colorDepth) {
            memInt[fbIndex] = colorDepth.color;
            if (depthOffset == 0) {
                memInt[depthIndex] = (memInt[depthIndex] & 0xFFFF0000) | (colorDepth.depth & 0x0000FFFF);
            } else {
                memInt[depthIndex] = (memInt[depthIndex] & 0x0000FFFF) | (colorDepth.depth << 16);
            }
            next();
        }

        @Override
        public void writeNextColor(int color) {
            memInt[fbIndex] = color;
            next();
        }

        @Override
        public void skip(int fbCount, int depthCount) {
            fbIndex += fbCount;
            depthOffset += depthCount;
            depthIndex += depthOffset >> 1;
            depthOffset &= 1;
        }

        @Override
        public void flush() {
        }
    }

    private static final class RendererWriterNoDepthWriteInt32 implements IRendererWriter {

        private int fbIndex;

        private int depthIndex;

        private int depthOffset;

        private final int[] memInt;

        public RendererWriterNoDepthWriteInt32(int[] memInt, int fbAddress, int depthAddress) {
            this.memInt = memInt;
            fbIndex = (fbAddress & Memory.addressMask) >> 2;
            depthIndex = (depthAddress & Memory.addressMask) >> 2;
            depthOffset = (depthAddress >> 1) & 1;
        }

        @Override
        public void readCurrent(ColorDepth colorDepth) {
            colorDepth.color = memInt[fbIndex];
            if (depthOffset == 0) {
                colorDepth.depth = memInt[depthIndex] & 0x0000FFFF;
            } else {
                colorDepth.depth = memInt[depthIndex] >>> 16;
            }
        }

        private void next() {
            fbIndex++;
            if (depthOffset == 0) {
                depthOffset = 1;
            } else {
                depthIndex++;
                depthOffset = 0;
            }
        }

        @Override
        public void writeNext(ColorDepth colorDepth) {
            memInt[fbIndex] = colorDepth.color;
            next();
        }

        @Override
        public void writeNextColor(int color) {
            memInt[fbIndex] = color;
            next();
        }

        @Override
        public void skip(int fbCount, int depthCount) {
            fbIndex += fbCount;
            depthOffset += depthCount;
            depthIndex += depthOffset >> 1;
            depthOffset &= 1;
        }

        @Override
        public void flush() {
        }
    }

    private static final class RendererWriterNoDepthReadWriteInt32 implements IRendererWriter {

        private int fbIndex;

        private final int[] memInt;

        public RendererWriterNoDepthReadWriteInt32(int[] memInt, int fbAddress) {
            this.memInt = memInt;
            fbIndex = (fbAddress & Memory.addressMask) >> 2;
        }

        @Override
        public void readCurrent(ColorDepth colorDepth) {
            colorDepth.color = memInt[fbIndex];
        }

        @Override
        public void writeNext(ColorDepth colorDepth) {
            memInt[fbIndex] = colorDepth.color;
            fbIndex++;
        }

        @Override
        public void writeNextColor(int color) {
            memInt[fbIndex] = color;
            fbIndex++;
        }

        @Override
        public void skip(int fbCount, int depthCount) {
            fbIndex += fbCount;
        }

        @Override
        public void flush() {
        }
    }
}
