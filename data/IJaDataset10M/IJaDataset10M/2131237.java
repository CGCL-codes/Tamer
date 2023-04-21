package codesounding.filter;

import codesounding.dynamic.BasicProcessor;

public class SheetMusicFilter extends Filter {

    protected final String MAX_ROWS = "midi.totvox";

    protected final String MIDI_INSTRUMENT = "midi.instrument.voxnum";

    private boolean isFirstRow = true;

    private int rowCounter = 1;

    private int maxRows = 1;

    private int maxRowLength = 52;

    protected int getMaxRows() {
        int res = maxRows;
        String strMaxRows = BasicProcessor.getInstance().getConfigProperty(MAX_ROWS).trim();
        if (!strMaxRows.equals("")) {
            try {
                res = Integer.parseInt(strMaxRows);
            } catch (Exception e) {
            }
        }
        return res;
    }

    private String getInstrument(int rowNumber) {
        String command = "";
        String val = BasicProcessor.getInstance().getConfigProperty(MIDI_INSTRUMENT + "." + rowNumber);
        if (val.equals("") && getMaxRows() == 1) {
            val = BasicProcessor.getInstance().getConfigProperty(MIDI_INSTRUMENT).trim();
        }
        if (!val.equals("")) {
            command = " [I:MIDI = program " + val + "] ";
        }
        return command;
    }

    protected void filter() {
        try {
            int size = mBuffer.size();
            if (size % 4 == 0) {
                mBuffer.write("|".getBytes());
            }
            int modulo = size % maxRowLength;
            if (size >= maxRowLength && modulo < 3) {
                if (getMaxRows() > 1) {
                    if (isFirstRow) {
                        byte[] tmp = mBuffer.toByteArray();
                        mBuffer.reset();
                        mBuffer.write(("[V: 1] " + getInstrument(1) + " ").getBytes());
                        mBuffer.write(tmp);
                        mBuffer.write("\r\n".getBytes());
                        mBuffer.write(("[V: 2] " + getInstrument(2) + " ").getBytes());
                        rowCounter++;
                        isFirstRow = false;
                    } else {
                        mBuffer.write("\r\n".getBytes());
                        mBuffer.write(("[V: " + rowCounter + "] " + getInstrument(rowCounter) + " ").getBytes());
                    }
                    rowCounter++;
                    if (rowCounter > getMaxRows()) {
                        rowCounter = 1;
                    }
                } else {
                    mBuffer.write("\r\n".getBytes());
                }
                setToFlush(true);
            }
        } catch (Exception e) {
        }
    }
}
