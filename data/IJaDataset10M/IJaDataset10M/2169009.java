package as.ide.ui.editors;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

public class ASFormatStrategy implements IFormattingStrategy {

    @Override
    public String format(String content, boolean isLineStart, String indentation, int[] positions) {
        return null;
    }

    @Override
    public void formatterStarts(String initialIndentation) {
        System.out.println("editors.ASFormatStrategy: " + initialIndentation);
    }

    @Override
    public void formatterStops() {
    }
}
