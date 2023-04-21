package vue;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * vue de l'aide
 *
 */
public class HelpView extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final int EN = 0;

    public static final int FR = 1;

    /**
	 * constructeur
	 *
	 */
    public HelpView(int lang) {
        JTextArea text = new JTextArea();
        String s = "";
        String ligne;
        String fileName;
        switch(lang) {
            case EN:
                fileName = "help-en.txt";
                break;
            case FR:
                fileName = "help.txt";
                break;
            default:
                fileName = "help.txt";
        }
        File file = new File(fileName);
        InputStream ips;
        try {
            ips = new FileInputStream(file);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            try {
                while ((ligne = br.readLine()) != null) {
                    s = s + ligne + "\n";
                }
            } catch (IOException e) {
            }
            text.setText(s);
        } catch (FileNotFoundException e) {
            System.out.println("fihcier non trouvé " + fileName);
        }
        text.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(text);
        jScrollPane.setPreferredSize(new Dimension(700, 110));
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.getViewport().add(text, null);
        this.add(jScrollPane);
    }
}
