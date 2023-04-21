package model.datapath.utils;

/**
 * The combination of a mux and demux.<br>
 * When used in combination in a single data stream, these can be used to easily
 * demux and then mux the stream without having to worry about data stream size
 * requirements.<br>
 * To realise this, the mux fills up the (possible) empty positions in the last
 * transmitted block element. These positions are filled with references to the
 * last filled element of the block, e.g. if a stream consisting of Chars the
 * last block would logically contain [a,b,null,null], it would be transmitted
 * as [a,b,b,b]. Additionally, if (and only if) the demux receives its last
 * block after the mux has received it, it will only transmit as much elements
 * from this last block as the demux has received in its last block. For the
 * last example, this would mean that only the first two elements (a and b)
 * would be transmitted by the demux.
 * 
 * @author gijs
 * 
 * @param <T>
 *            the element type to transmit
 */
public class MuxDemuxCombo<T> {

    private Mux<T> mux;

    private Demux<T> demux;

    private int lastPos;

    public MuxDemuxCombo(int blocksize, Class<?> clazz) {
        mux = new MyMux(blocksize, clazz);
        demux = new MyDemux();
    }

    public Mux<T> getMux() {
        return mux;
    }

    public Demux<T> getDemux() {
        return demux;
    }

    private class MyMux extends Mux<T> {

        public MyMux(int blocksize, Class<?> c) {
            super(blocksize, c);
        }

        @Override
        public void wroteLast() {
            lastPos = getPos();
            if (getPos() != 0) {
                T lastElem = getBuffer()[getPos() - 1];
                for (int i = getPos(); i < getBlockSize(); i++) {
                    getBuffer()[i] = lastElem;
                }
                getNext().write(getBuffer());
            }
            getNext().wroteLast();
        }
    }

    private class MyDemux extends Demux<T> {

        private T[] buffer;

        @Override
        public void write(T[] elems) {
            if (buffer != null) for (T elem : buffer) getNext().write(elem);
            buffer = elems;
        }

        @Override
        public void wroteLast() {
            if (buffer == null) return;
            if (lastPos == 0) lastPos = buffer.length;
            for (int i = 0; i < lastPos; i++) {
                getNext().write(buffer[i]);
            }
            super.wroteLast();
        }
    }
}
