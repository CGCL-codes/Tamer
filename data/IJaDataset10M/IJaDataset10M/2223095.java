package com.google.code.cubeirc.dialogs;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import com.google.code.cubeirc.Main;
import com.google.code.cubeirc.editor.EditorManager;

public class ColorsChooserAdapter extends KeyAdapter {

    @Getter
    @Setter
    private EditorManager edit;

    private ColorsChooser cs;

    private static Boolean isactive = false;

    public ColorsChooserAdapter(EditorManager edit) {
        setEdit(edit);
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if ((arg0.stateMask & SWT.CTRL) != 0 && arg0.keyCode == 'w') {
            if (!isactive) {
                cs = new ColorsChooser(Main.getShell(), SWT.NONE, getEdit().getEditor());
                isactive = true;
                String res = cs.open().toString();
                isactive = false;
                getEdit().addToEditorWithoutFormat(res);
            } else {
                cs.shell.setFocus();
            }
        }
        super.keyPressed(arg0);
    }
}
