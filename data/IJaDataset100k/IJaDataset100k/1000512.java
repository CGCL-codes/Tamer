package free.jin.board.prefs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import free.chess.JBoard;
import free.jin.BadChangesException;
import free.jin.board.BoardManager;
import free.jin.board.JinBoard;
import free.util.swing.ColorChooser;
import free.util.swing.PreferredSizedPanel;

/**
 * A preferences panel allowing the user to select the square coordinates
 * settings.
 */
public class SquareCoordinatesPanel extends BoardModifyingPrefsPanel {

    /**
   * The radio button for no move square coordinates.
   */
    private final JRadioButton none;

    /**
   * The radio button for square coordinates on the rim of the board
   */
    private final JRadioButton rim;

    /**
   * The radio button for square coordinates outside the board.
   */
    private final JRadioButton outside;

    /**
   * The radio button for square coordinates in every square.
   */
    private final JRadioButton everySquare;

    /**
   * The color chooser for color of the square coordinates' text.
   */
    private final ColorChooser coordsColor;

    /**
   * Creates a new SquareCoordinatesPanel for the specified BoardManager and
   * with the specified preview board.
   */
    public SquareCoordinatesPanel(BoardManager boardManager, JinBoard previewBoard) {
        super(boardManager, previewBoard);
        int coordsDisplayStyle = boardManager.getCoordsDisplayStyle();
        none = new JRadioButton("None", coordsDisplayStyle == JBoard.NO_COORDS);
        rim = new JRadioButton("On the rim of the board (like winboard)", coordsDisplayStyle == JBoard.RIM_COORDS);
        outside = new JRadioButton("Outside the board", coordsDisplayStyle == JBoard.OUTSIDE_COORDS);
        everySquare = new JRadioButton("In every square", coordsDisplayStyle == JBoard.ARROW_MOVE_HIGHLIGHTING);
        coordsColor = new ColorChooser("Coordinates' text color:", boardManager.getCoordsDisplayColor());
        none.setToolTipText("No square coordinates are displayed on the board");
        rim.setToolTipText("Row and column coordinates are displayed on the " + "border of the leftmost and bottommost squares");
        outside.setToolTipText("Row and column coordinates are displayed outside " + "the board, along its left and bottom borders");
        everySquare.setToolTipText("Square coordinates are displayed in each square");
        coordsColor.setToolTipText("The color of the coordinates' text");
        ButtonGroup group = new ButtonGroup();
        group.add(none);
        group.add(rim);
        group.add(outside);
        group.add(everySquare);
        none.setMnemonic('N');
        rim.setMnemonic('r');
        outside.setMnemonic('O');
        everySquare.setMnemonic('I');
        coordsColor.setMnemonic('C');
        setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel contentPanel = new PreferredSizedPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Square Coordinates"), BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        none.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        rim.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        outside.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        everySquare.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        coordsColor.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        contentPanel.add(none);
        contentPanel.add(rim);
        contentPanel.add(outside);
        contentPanel.add(everySquare);
        contentPanel.add(coordsColor);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        add(contentPanel);
        ActionListener styleListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                SquareCoordinatesPanel.this.previewBoard.setCoordsDisplayStyle(getCoordsDisplayStyle());
                fireStateChanged();
            }
        };
        none.addActionListener(styleListener);
        rim.addActionListener(styleListener);
        outside.addActionListener(styleListener);
        everySquare.addActionListener(styleListener);
        coordsColor.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                SquareCoordinatesPanel.this.previewBoard.setCoordsDisplayColor(coordsColor.getColor());
                fireStateChanged();
            }
        });
    }

    /**
   * Returns the currently selected coordinates display style.
   */
    private int getCoordsDisplayStyle() {
        if (none.isSelected()) return JBoard.NO_COORDS; else if (rim.isSelected()) return JBoard.RIM_COORDS; else if (outside.isSelected()) return JBoard.OUTSIDE_COORDS; else if (everySquare.isSelected()) return JBoard.EVERY_SQUARE_COORDS; else throw new IllegalStateException("None of the radio buttons are selected");
    }

    /**
   * Sets the initial properties of the preview board.
   */
    public void initPreviewBoard() {
        previewBoard.setCoordsDisplayStyle(getCoordsDisplayStyle());
        previewBoard.setCoordsDisplayColor(coordsColor.getColor());
    }

    /**
   * Applies any changes made by the user.
   */
    public void applyChanges() throws BadChangesException {
        boardManager.setCoordsDisplayStyle(getCoordsDisplayStyle());
        boardManager.setCoordsDisplayColor(coordsColor.getColor());
    }
}
