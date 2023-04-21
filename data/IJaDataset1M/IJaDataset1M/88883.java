package org.doit.muffin.filter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import org.doit.muffin.*;
import org.doit.util.*;

public class RewriteFrame extends MuffinFrame implements ActionListener, WindowListener {

    /**
	 * Serializable should define this:
	 */
    private static final long serialVersionUID = 1L;

    Prefs prefs;

    Rewrite parent;

    TextField input = null;

    TextArea text = null;

    public RewriteFrame(Prefs prefs, Rewrite parent) {
        super(Strings.getString("Rewrite.title"));
        this.prefs = prefs;
        this.parent = parent;
        Panel panel = new Panel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c;
        panel.add(new Label(Strings.getString("Rewrite.rules") + ":", Label.RIGHT));
        input = new TextField(40);
        input.setText(prefs.getString("Rewrite.rules"));
        panel.add(input);
        Button browse = new Button(Strings.getString("browse") + "...");
        browse.setActionCommand("doBrowse");
        browse.addActionListener(this);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        layout.setConstraints(browse, c);
        panel.add(browse);
        add(BorderLayout.NORTH, panel);
        panel = new Panel();
        layout = new GridBagLayout();
        panel.setLayout(layout);
        Label l = new Label(Strings.getString("Rewrite.header"));
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 5, 10);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        layout.setConstraints(l, c);
        panel.add(l);
        text = new TextArea();
        c = new GridBagConstraints();
        c.gridheight = 3;
        c.insets = new Insets(0, 10, 5, 10);
        layout.setConstraints(text, c);
        panel.add(text);
        Button b;
        b = new Button(Strings.getString("apply"));
        b.setActionCommand("doApply");
        b.addActionListener(this);
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        layout.setConstraints(b, c);
        panel.add(b);
        b = new Button(Strings.getString("load"));
        b.setActionCommand("doLoad");
        b.addActionListener(this);
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        layout.setConstraints(b, c);
        panel.add(b);
        b = new Button(Strings.getString("save"));
        b.setActionCommand("doSave");
        b.addActionListener(this);
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        layout.setConstraints(b, c);
        panel.add(b);
        l = new Label(Strings.getString("Rewrite.messages"));
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 5, 10);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        layout.setConstraints(l, c);
        panel.add(l);
        c = new GridBagConstraints();
        c.insets = new Insets(0, 10, 5, 10);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        layout.setConstraints(parent.messages, c);
        parent.messages.setEditable(false);
        panel.add(parent.messages);
        add(BorderLayout.CENTER, panel);
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        b = new Button(Strings.getString("clear"));
        b.setActionCommand("doClear");
        b.addActionListener(this);
        buttonPanel.add(b);
        b = new Button(Strings.getString("close"));
        b.setActionCommand("doClose");
        b.addActionListener(this);
        buttonPanel.add(b);
        b = new Button(Strings.getString("help"));
        b.setActionCommand("doHelp");
        b.addActionListener(this);
        buttonPanel.add(b);
        add(BorderLayout.SOUTH, buttonPanel);
        addWindowListener(this);
        pack();
        setSize(getPreferredSize());
        loadFile();
        show();
    }

    void loadFile() {
        text.setText("");
        UserFile file = prefs.getUserFile(prefs.getString("Rewrite.rules"));
        InputStream in = null;
        try {
            in = file.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s;
            while ((s = br.readLine()) != null) {
                text.append(s + "\n");
            }
            br.close();
            in.close();
            in = null;
            text.setCaretPosition(0);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    void saveFile() {
        try {
            UserFile file = prefs.getUserFile(prefs.getString("Rewrite.rules"));
            if (file instanceof LocalFile) {
                LocalFile f = (LocalFile) file;
                f.delete();
                OutputStream out = file.getOutputStream();
                Writer writer = new OutputStreamWriter(out);
                writer.write(text.getText());
                writer.close();
                out.close();
            } else {
                Dialog d = new ErrorDialog(this, "Can't save to " + file.getName());
                d.setVisible(true);
                d.dispose();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void actionPerformed(ActionEvent event) {
        String arg = event.getActionCommand();
        if ("doApply".equals(arg)) {
            prefs.putString("Rewrite.rules", input.getText());
            parent.load(new StringReader(text.getText()));
        } else if ("doSave".equals(arg)) {
            parent.save();
            saveFile();
        } else if ("doLoad".equals(arg)) {
            parent.load();
            loadFile();
        } else if ("doClear".equals(arg)) {
            parent.messages.clear();
        } else if ("doClose".equals(arg)) {
            setVisible(false);
        } else if ("doBrowse".equals(arg)) {
            FileDialog dialog = new FileDialog(this, "Rewrite Load");
            dialog.setVisible(true);
            if (dialog.getFile() != null) {
                input.setText(dialog.getDirectory() + dialog.getFile());
            }
        } else if ("doHelp".equals(arg)) {
            new HelpFrame("Rewrite");
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        setVisible(false);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
