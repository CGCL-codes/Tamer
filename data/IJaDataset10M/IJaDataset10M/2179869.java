package org.fest.swing.test.builder;

import java.awt.Frame;
import javax.swing.JDialog;
import org.fest.swing.annotation.RunsInCurrentThread;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.GuiQuery;
import static org.fest.swing.edt.GuiActionRunner.execute;

/**
 * Understands creation of <code>{@link JDialog}</code>s.
 *
 * @author Alex Ruiz
 */
public final class JDialogs {

    private JDialogs() {
    }

    public static JDialogFactory dialog() {
        return new JDialogFactory();
    }

    public static class JDialogFactory {

        private String name;

        private Frame owner;

        private boolean resizable = true;

        private String title;

        public JDialogFactory withOwner(Frame newOwner) {
            owner = newOwner;
            return this;
        }

        public JDialogFactory withName(String newName) {
            name = newName;
            return this;
        }

        public JDialogFactory withTitle(String newTitle) {
            title = newTitle;
            return this;
        }

        public JDialogFactory resizable(boolean shouldBeResizable) {
            resizable = shouldBeResizable;
            return this;
        }

        @RunsInEDT
        public JDialog createNew() {
            return execute(new GuiQuery<JDialog>() {

                protected JDialog executeInEDT() {
                    return create();
                }
            });
        }

        @RunsInEDT
        public JDialog createAndShow() {
            return execute(new GuiQuery<JDialog>() {

                protected JDialog executeInEDT() {
                    JDialog dialog = create();
                    dialog.pack();
                    dialog.setVisible(true);
                    return dialog;
                }
            });
        }

        @RunsInCurrentThread
        JDialog create() {
            JDialog dialog = owner != null ? new JDialog(owner) : new JDialog();
            dialog.setName(name);
            dialog.setTitle(title);
            dialog.setResizable(resizable);
            return dialog;
        }
    }
}
