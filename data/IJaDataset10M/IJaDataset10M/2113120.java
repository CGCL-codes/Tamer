package Forms.StdComponents;

import java.awt.*;
import java.util.StringTokenizer;

/** Komponente zur Eingabe des Preises d. Platzgruppen */
public class SeatCategoryInputSlider extends InputBarComponent {

    private Panel p;

    private InputSlider input1;

    private InputSlider input2;

    /** - erzeugt neue Eingabe <br>
      - s1 - Beschriftung <br>
      - s2 - Hilfe */
    public SeatCategoryInputSlider(String s1, String s2) {
        super(s1, s2);
        p = new Panel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(Color.lightGray);
        input1 = new InputSlider(0, 20, "Durch Dr�cken der + und - Buttons DM-Anteil des Preises eingeben", true);
        input2 = new InputSlider(0, 99, "Durch Dr�cken der + und - Buttons Pfenniganteil des Preises eingeben", true);
        p.add(input1);
        p.add(new Label("DM   .  "));
        p.add(input2);
        p.add(new Label("Pfennig"));
    }

    /** liefert zugeh. AWT-Komponente */
    public Component getComponent() {
        return p;
    }

    /** liefert akt. Eintrag */
    public String getText() {
        return input1.getText() + "." + input2.getText();
    }

    /** setzt Eintrag auf Wert s */
    public void setText(String s) {
        StringTokenizer st = new StringTokenizer(s, ".");
        String m = st.nextToken();
        String str = "";
        if (m.length() == 1) str = "0" + m; else str = m;
        input1.setText(str);
        String pf = st.nextToken();
        str = "";
        if (pf.length() == 1) str = pf + "0"; else str = pf;
        input2.setText(str);
    }

    /** l�scht Eintrag */
    public void clear() {
        input1.setText("00");
        input2.setText("00");
    }
}
