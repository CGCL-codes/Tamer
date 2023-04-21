package mipt.gui.choice;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import mipt.gui.Display;

/**
 * CellEditor that can show "big" separate component editing or choosing values in them
 * Can get both compex edited object and simple-type representations (see setReturnSimpleType())   
 * @author Evdokimov
 */
public class ChoosableCellEditor extends DefaultCellEditor {

    private static final long serialVersionUID = -2097388803506733023L;

    private boolean returnSimpleType = true;

    /**
	 * JTextField (and JFileChooser) can be null
	 */
    public ChoosableCellEditor(JFileChooser chooser, JTextField editor) {
        super();
        if (chooser == null) chooser = new JFileChooser();
        if (editor == null) editor = new JTextField();
        initFile(chooser, editor);
        initCancelCellEditing(editor);
    }

    /**
	 * JColorChooser can (and even should!) be null: that class is accesses statically
	 * Note: we can't cancel cell editing by editor component (it is Label) 
	 */
    public ChoosableCellEditor(JColorChooser chooser) {
        super();
        initColor();
    }

    /**
	 * @return returnSimpleType
	 */
    public final boolean isReturnSimpleType() {
        return returnSimpleType;
    }

    /**
	 * Set false if getCellEditorValue() should return File, Color or other complex objects
	 * By default Strings and int codes are returned
	 * @param returnSimpleType
	 */
    public void setReturnSimpleType(boolean returnSimpleType) {
        this.returnSimpleType = returnSimpleType;
    }

    protected void initFile(final JFileChooser chooser, final JTextField editor) {
        this.editorComponent = editor;
        this.delegate = new EditorDelegate() {

            public void setValue(Object value) {
                if (chooseFile(chooser, value) != JFileChooser.APPROVE_OPTION) return;
                editor.setText(chooser.getSelectedFile() != null ? chooser.getSelectedFile().getPath() : getNullText());
            }

            public Object getCellEditorValue() {
                return getFile(editor.getText());
            }
        };
    }

    protected Object getFile(String text) {
        if (text.equals(NULL_VALUE)) return null;
        if (returnSimpleType) return text;
        return new File(text);
    }

    protected int chooseFile(JFileChooser chooser, Object value) {
        File file = null;
        if (value instanceof File) file = (File) value; else if (value != null) file = new File(value.toString());
        chooser.setSelectedFile(file);
        if (file != null && file.isFile()) file = file.getParentFile();
        chooser.setCurrentDirectory(file);
        return chooser.showOpenDialog(getParentComponent());
    }

    protected void initColor() {
        this.editorComponent = new JLabel();
        editorComponent.setOpaque(true);
        this.delegate = new EditorDelegate() {

            public void setValue(Object value) {
                Color color = chooseColor(value);
                if (color == null) return;
                editorComponent.setBackground((Color) value);
            }

            public Object getCellEditorValue() {
                return getColor();
            }
        };
    }

    protected Object getColor() {
        Color color = editorComponent.getBackground();
        if (returnSimpleType) return new Integer(color.getRGB());
        return color;
    }

    protected Color chooseColor(Object value) {
        Color color = null;
        if (value instanceof Color) color = (Color) value; else if (value instanceof Integer) color = new Color(((Integer) value).intValue());
        return JColorChooser.showDialog(getParentComponent(), null, color);
    }

    protected Component getParentComponent() {
        return getDisplay().getFrame();
    }

    public Display getDisplay() {
        return Display.getInstance();
    }
}
