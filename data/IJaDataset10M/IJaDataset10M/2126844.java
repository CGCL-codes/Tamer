package pdp.scrabble.ihm;

import static pdp.scrabble.game.BoardCase.CASE_SIZE;
import static pdp.scrabble.game.Rack.MAX_RACK_LETTERS;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import pdp.scrabble.game.Letter;
import pdp.scrabble.game.Rack;

@SuppressWarnings("serial")
public class RackView extends JPanel {

    /** Selected case color. */
    private static final Color SELECTED_RACK_LETTER_COLOR = new Color(0, 0, 0, 160);

    /** Selected case color for reset. */
    private static final Color SELECTED_RACK_LETTER_RESET_COLOR = new Color(255, 0, 0, 128);

    /** Selected case color. */
    private static final Color SELECTED_CASE_COLOR = new Color(0, 128, 0, 160);

    /** Rack color. */
    private static final Color RACK_COLOR = new Color(204, 153, 51);

    private final int L1 = 7;

    private final int L2 = 426;

    private final int S1 = 9;

    private final int H1 = 45;

    private final int H2 = 11;

    private Rack rack;

    private int[] ordre;

    private Integer selected;

    private Integer hovered;

    private List<Integer> toReset;

    public RackView(Rack r) {
        this.rack = r;
        this.ordre = new int[MAX_RACK_LETTERS];
        for (int i = 0; i < rack.getNumberOfLetters(); i++) ordre[i] = i;
        selected = -1;
        hovered = -1;
        this.toReset = new ArrayList<Integer>();
        this.setPreferredSize(new Dimension(L2 + 2 * S1 + 2 * L1 + 24, H1 + H2 + 20));
    }

    public void changeRack(Rack r) {
        this.rack = r;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int i) {
        if (i < MAX_RACK_LETTERS) selected = i;
    }

    public int getHovered() {
        return hovered;
    }

    public void setHovered(int i) {
        if (i < MAX_RACK_LETTERS) hovered = i;
    }

    public void addReset(int i) {
        toReset.add(i);
    }

    public void removeReset(int i) {
        toReset.remove(new Integer(i));
    }

    public boolean isToReset(int i) {
        return toReset.contains(i);
    }

    public boolean hasLettersToReset() {
        return toReset.isEmpty();
    }

    public void swapLetters(int i, int j) {
        int tmp = ordre[j];
        ordre[j] = ordre[i];
        ordre[i] = tmp;
        if (isToReset(i) && !isToReset(j)) {
            addReset(j);
            removeReset(i);
        } else if (isToReset(j) && !isToReset(i)) {
            addReset(i);
            removeReset(j);
        }
    }

    public Letter getLetterAtIndex(int i) {
        return rack.getLetter(ordre[i]);
    }

    private int getLetterX(int i) {
        return 16 + 26 + i * 64;
    }

    private int getLetterY(int i) {
        return 20;
    }

    public void hoveredAt(int mouseX, int mouseY) {
        for (int i = 0; i < MAX_RACK_LETTERS; i++) {
            int x = getLetterX(i);
            int y = getLetterY(i);
            if (mouseX > x && mouseX < x + CASE_SIZE && mouseY > y && mouseY < y + CASE_SIZE) hovered = i;
        }
    }

    /** returns index of the letter at coords x and y. returns -1 if none. */
    public int getLetterIndexAt(int mouseX, int mouseY) {
        for (int i = 0; i < MAX_RACK_LETTERS; i++) {
            int x = getLetterX(i);
            int y = getLetterY(i);
            if (mouseX > x && mouseX < x + CASE_SIZE && mouseY > y && mouseY < y + CASE_SIZE) return i;
        }
        return -1;
    }

    private void paintRack(Graphics g, int x, int y) {
        g.translate(x, y);
        g.setColor(RACK_COLOR);
        g.fill3DRect(S1 + L1, 0, L2, H1, true);
        g.fill3DRect(S1, 0, L1, H1, true);
        g.fill3DRect(L1 + S1 + L2, 0, L1, H1, true);
        g.fill3DRect(0, H1, L2 + 2 * S1 + 2 * L1, H2, true);
        g.translate(-x, -y);
    }

    @Override
    public void paintComponent(Graphics g) {
        int ox = 26;
        int oy = 4;
        paintRack(g, ox, 12 + oy);
        for (int i = 0; i < MAX_RACK_LETTERS; i++) {
            Letter letter = rack.getLetter(ordre[i]);
            int x = 16 + ox + i * 64;
            int y = 16 + oy;
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CASE_SIZE, CASE_SIZE);
            if (letter != null) {
                letter.render(g, x, y);
                if (i == selected) {
                    g.setColor(SELECTED_CASE_COLOR);
                    g.fillRect(x, y, CASE_SIZE, CASE_SIZE);
                }
                if (i == hovered) {
                    g.setColor(SELECTED_RACK_LETTER_COLOR);
                    g.fillRect(x, y, CASE_SIZE, CASE_SIZE);
                }
            }
        }
        for (Integer index : toReset) {
            Letter letterToReset = rack.getLetter(ordre[index]);
            if (letterToReset != null) {
                int x = 16 + ox + index * 64;
                int y = 16 + oy;
                g.setColor(SELECTED_RACK_LETTER_RESET_COLOR);
                g.fillRect(x, y, CASE_SIZE, CASE_SIZE);
            }
        }
    }
}
