package gnagck.block;

import gnagck.util.SelectionManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * Displays all available Blocks.  The user may select and edit Blocks.
 * @author royer
 *
 */
public class BlockEnumeratorPanel extends JPanel implements MouseListener, ActionListener {

    /**
	 * auto generated ID
	 */
    private static final long serialVersionUID = 533665401035862410L;

    /**
	 * The number of pixels on each side of the Block.  Should be odd.
	 */
    private static final int padding = 5;

    /**
	 * The number of rows of blocks to display
	 */
    private static final int rows = 4;

    /**
	 * A context menu
	 */
    private JPopupMenu popupMenu;

    /**
	 * New Block menu item for the context menu
	 */
    private JMenuItem newBlockMenuItem;

    /**
	 * Constructor.
	 */
    public BlockEnumeratorPanel() {
        setLayout(null);
        popupMenu = new JPopupMenu();
        newBlockMenuItem = new JMenuItem("New..");
        newBlockMenuItem.addActionListener(this);
        popupMenu.add(newBlockMenuItem);
        addMouseListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        BlockManager manager = BlockManager.getInstance();
        List<Block> blocks = manager.getBlocks();
        Dimension d = new Dimension((int) Math.ceil((padding * 2 + Block.getWidth()) * (blocks.size() / (double) rows)), (padding * 2 + Block.getHeight()) * rows);
        if (d.getWidth() < (padding * 2 + Block.getWidth()) * 10 || d.getHeight() < (padding * 2 + Block.getHeight()) * 4) {
            return new Dimension((padding * 2 + Block.getWidth()) * 10, (padding * 2 + Block.getHeight()) * 4);
        }
        return d;
    }

    @Override
    public Dimension getSize(Dimension arg0) {
        return getPreferredSize();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        BlockManager manager = BlockManager.getInstance();
        List<Block> blocks = manager.getBlocks();
        int blockNum = 0;
        Block selectedBlock = SelectionManager.getInstance().getSelectedBlock();
        for (Block b : blocks) {
            AffineTransform t = g2.getTransform();
            AffineTransform old = new AffineTransform(t);
            t.translate((padding * 2 + Block.getWidth()) * (blockNum / rows) + padding, (padding * 2 + Block.getHeight()) * (blockNum % rows) + padding);
            g2.setTransform(t);
            if (b == selectedBlock) {
                int offset = padding / 2 + 1;
                g2.setColor(Color.black);
                g2.drawRect(-offset, -offset, padding + Block.getWidth(), padding + Block.getHeight());
            }
            b.draw(g2);
            g2.setTransform(old);
            blockNum++;
        }
    }

    public void mouseClicked(MouseEvent event) {
        Block selectedBlock = SelectionManager.getInstance().getSelectedBlock();
        if (event.getClickCount() == 2) {
            if (selectedBlock == null) {
                return;
            }
            BlockEditDialog dlg = new BlockEditDialog(this, selectedBlock);
            dlg.setModal(true);
            dlg.setTitle("Edit block...");
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
            if (!dlg.isCancelled()) {
            }
            repaint();
            return;
        }
        BlockManager manager = BlockManager.getInstance();
        List<Block> blocks = manager.getBlocks();
        int blockNum = mouse2Block(event.getPoint());
        if (blockNum < 0 || blockNum >= blocks.size()) {
            return;
        }
        SelectionManager.getInstance().setSelectedBlock(blocks.get(blockNum));
        repaint();
    }

    /**
	 * Converts mouse position to Block index.
	 * @param p The mouse position in pixels
	 * @return The Block index 
	 */
    private int mouse2Block(Point p) {
        int x = p.x;
        int y = p.y;
        int blockNum = x / (padding * 2 + Block.getWidth()) * rows + y / (padding * 2 + Block.getHeight());
        return blockNum;
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent event) {
        if (event.isPopupTrigger()) {
            popupMenu.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    public void mouseReleased(MouseEvent event) {
        if (event.isPopupTrigger()) {
            popupMenu.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == newBlockMenuItem) {
            BlockManager.getInstance().createBlock();
            getParent().doLayout();
            repaint();
        }
    }
}
