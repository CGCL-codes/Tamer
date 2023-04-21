package uk.org.ogsadai.activity.block;

import java.util.LinkedList;
import java.util.List;
import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * TODO: document.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class IfEmptyListActivity extends ActivityBase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010";

    public static final String INPUT = "input";

    public static final String TRUE_OUTPUT = "trueOutput";

    public static final String FALSE_OUTPUT = "falseOutput";

    public void process() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateOutput(TRUE_OUTPUT);
        validateOutput(FALSE_OUTPUT);
        validateInput(INPUT);
        BlockReader input = getInput(INPUT);
        BlockWriter trueOutput = getOutput(TRUE_OUTPUT);
        BlockWriter falseOutput = getOutput(FALSE_OUTPUT);
        try {
            Object block;
            boolean listHasData = false;
            int listDepth = 0;
            List<Object> readData = new LinkedList<Object>();
            while ((block = input.read()) != ControlBlock.NO_MORE_DATA) {
                if (!listHasData) {
                    readData.add(block);
                } else {
                    falseOutput.write(block);
                }
                if (block == ControlBlock.LIST_BEGIN) {
                    listDepth++;
                    if (listDepth > 1 && !listHasData) {
                        listHasData = true;
                        writeDataBlocks(readData, falseOutput);
                        readData.clear();
                    }
                } else if (block == ControlBlock.LIST_END) {
                    listDepth--;
                    if (listDepth == 0) {
                        if (listHasData) {
                            listHasData = false;
                            readData.clear();
                        } else {
                            writeDataBlocks(readData, trueOutput);
                            readData.clear();
                        }
                    }
                } else if (!(block instanceof MetadataWrapper) && !listHasData) {
                    listHasData = true;
                    writeDataBlocks(readData, falseOutput);
                    readData.clear();
                }
            }
        } catch (PipeClosedException e) {
        } catch (PipeIOException e) {
            throw new ActivityProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }
    }

    private void writeDataBlocks(List<Object> blocks, BlockWriter output) throws PipeClosedException, PipeIOException, PipeTerminatedException {
        for (Object block : blocks) {
            output.write(block);
        }
    }
}
