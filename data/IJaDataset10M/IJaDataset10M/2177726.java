package graphics;

import javax.swing.*;
import main.GlobalVariables;
import storage.ClassNode;

public class SourceCodePopUp extends JPanel {

    private static final long serialVersionUID = 7422750144556008497L;

    private String src;

    private JTextArea textArea;

    /**
	 * Constructor that sets up the local variables
	 */
    public SourceCodePopUp(String ClassName, String Method) {
        for (int i = 0; i < GlobalVariables.CLASS.size(); i++) {
            ClassNode x = (ClassNode) GlobalVariables.CLASS.get(i);
            if (x.getName().getText().equals(ClassName)) for (int j = 0; j < x.getMethods().size(); j++) if (x.getMethods().get(j).getName().equals(Method)) textArea = new JTextArea(Method + x.getMethods().get(j).getSrc(), 30, 50);
        }
        JScrollPane JSP = new JScrollPane(textArea);
        JSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(JSP);
    }

    public String getCode() {
        return textArea.getText();
    }
}
