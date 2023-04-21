package cn.rui.powermanja.pane;

import static cn.rui.powermanja.Powermanja.OPTION_BOUNDS;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import cn.rui.powermanja.Pane;
import cn.rui.powermanja.Powermanja;
import cn.rui.powermanja.pane.option.Option;
import cn.rui.powermanja.pane.option.ScoreX;
import cn.rui.powermanja.util.ResourceReader;

/**
 * Option pane shows current available options of the player, as well as 
 * the bonus multipliers.
 */
public class OptionPane extends Pane {

    public static final String BACKDROP = "image/option.pcx";

    private Option[] options = new Option[11];

    private int option = -1;

    private ScoreX[] scoreXs = new ScoreX[2];

    private int scoreX = -1;

    public OptionPane(Powermanja powermanja, Dimension size, IndexColorModel colorModel) throws IOException {
        super(powermanja, size, colorModel);
        ResourceReader in = new ResourceReader(BACKDROP);
        {
            in.readPCX(((DataBufferByte) (getRaster().getDataBuffer())).getData());
            in.close();
        }
        for (int i = 0; i < options.length; i++) {
            options[i] = new Option(this, i);
        }
        for (int i = 0; i < scoreXs.length; i++) {
            scoreXs[i] = new ScoreX(this, i);
        }
    }

    public void activeOption(int option) {
        this.option = option;
    }

    public void activeScoreX(int scoreX) {
        this.scoreX = scoreX;
    }

    public void updateOption(int option, boolean open) {
        if (open) options[option].open(); else options[option].close();
    }

    /**
	 * Update the option pane, and repaint it only when necessary.
	 */
    @Override
    public void update(Graphics g) throws IOException {
        for (int i = 0; i < options.length; i++) {
            options[i].run(i == option);
        }
        for (int i = 0; i < scoreXs.length; i++) {
            scoreXs[i].run(i == scoreX);
        }
        g.drawImage(this, OPTION_BOUNDS.x, OPTION_BOUNDS.y, OPTION_BOUNDS.width, OPTION_BOUNDS.height, null);
    }
}
