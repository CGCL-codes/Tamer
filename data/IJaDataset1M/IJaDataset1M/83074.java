package com.yellowbkpk.jtah.pipeline;

import com.yellowbkpk.jtah.pipeline.command.PipelineCommand;
import com.yellowbkpk.jtah.pipeline.command.SplitterCommand;
import com.yellowbkpk.jtah.pipeline.command.UploadCommand;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class PNGSplitterPipelineNode extends AbstractPipelineNode {

    public PNGSplitterPipelineNode(BlockingQueue<PipelineCommand> inputPipe, BlockingQueue<PipelineCommand> outputPipe) {
        super(inputPipe, outputPipe);
    }

    public void run() {
        while (true) {
            try {
                Object dequeue = getInputPipe().take();
                System.err.println("Splitter dequeued " + dequeue);
                SplitterCommand comm = (SplitterCommand) dequeue;
                File inputImage = comm.getLargeImageFile();
                int tileX = comm.getTileX();
                int tileY = comm.getTileY();
                int tileZ = comm.getTileZ();
                String layer = comm.getLayer();
                int targetImgWidth = 256;
                int targetImgHeight = 256;
                BufferedImage sourceImage = ImageIO.read(inputImage);
                BufferedImage targetImage = new BufferedImage(targetImgWidth, targetImgHeight, BufferedImage.TYPE_INT_ARGB);
                int sourceWidth = sourceImage.getWidth();
                int sourceHeight = sourceImage.getHeight();
                for (int x = 0; x < sourceWidth; x += targetImgWidth) {
                    for (int y = 0; y < sourceHeight; y += targetImgHeight) {
                        BufferedImage subimage = sourceImage.getSubimage(x, y, targetImgWidth, targetImgHeight);
                        File outFile = new File(UUID.randomUUID() + ".png");
                        ImageIO.write(subimage, "PNG", outFile);
                        System.err.println("Saving chunk " + x + "," + y + " to " + outFile);
                        PipelineCommand uploadCommand = new UploadCommand(outFile, tileX, tileY, tileZ, layer);
                        getOutputPipe().add(uploadCommand);
                        tileY++;
                    }
                    tileX++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
