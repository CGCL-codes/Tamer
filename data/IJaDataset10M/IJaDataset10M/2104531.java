package annone.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import annone.ui.Chunk;
import annone.ui.CommonOptions;
import annone.ui.Options;
import annone.ui.Paragraph;
import annone.ui.Paragraph.Node;
import annone.ui.Paragraph.PictureNode;
import annone.ui.Paragraph.TextNode;
import annone.util.Checks;

public class SwingChunk extends SwingView implements Chunk {

    private static final boolean DEBUG_BORDERS = false;

    private static final String LINE_START_CLIENT_PROPERTY = SwingChunk.class.getName() + ".lineStart";

    private static final Object MARGINS_DEFAULT = new Insets(0, 0, 10, 0);

    private static class VisibleComponentsIterator implements Iterator<JComponent> {

        private final Container target;

        private final int componentCount;

        private JComponent current;

        private int nextIndex;

        private boolean first;

        public VisibleComponentsIterator(Container target) {
            this.target = target;
            this.componentCount = target.getComponentCount();
            this.nextIndex = -1;
            fetchNextIndex();
        }

        private void fetchNextIndex() {
            for (int i = nextIndex + 1; i < componentCount; i++) if (target.getComponent(i).isVisible()) {
                first = (nextIndex == -1);
                nextIndex = i;
                return;
            }
            nextIndex = componentCount;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < componentCount;
        }

        @Override
        public JComponent next() {
            if (nextIndex >= componentCount) throw new NoSuchElementException();
            current = (JComponent) target.getComponent(nextIndex);
            fetchNextIndex();
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean isLast() {
            return nextIndex == componentCount;
        }

        public boolean isNewLine() {
            if (current == null) throw new IllegalStateException();
            return !first && (current.getClientProperty(LINE_START_CLIENT_PROPERTY) == Boolean.TRUE);
        }
    }

    private static class ChunkLayout implements LayoutManager {

        private static class CompInfo {

            public JComponent component;

            public Rectangle bounds = new Rectangle();
        }

        private final Options options;

        public ChunkLayout(Options options) {
            this.options = options;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            Checks.instanceOf("comp", comp, JComponent.class);
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            synchronized (target.getTreeLock()) {
                Dimension prefSize = new Dimension(0, 0);
                Dimension rowSize = new Dimension(0, 0);
                VisibleComponentsIterator it = new VisibleComponentsIterator(target);
                while (it.hasNext()) {
                    JComponent comp = it.next();
                    if (it.isNewLine()) {
                        prefSize.width = Math.max(prefSize.width, rowSize.width);
                        prefSize.height += rowSize.height;
                        rowSize.width = rowSize.height = 0;
                    }
                    Dimension compSize = comp.getPreferredSize();
                    rowSize.width += compSize.width;
                    rowSize.height = Math.max(rowSize.height, compSize.height);
                    if (it.isLast()) {
                        prefSize.width = Math.max(prefSize.width, rowSize.width);
                        prefSize.height += rowSize.height;
                    }
                }
                Insets insets = target.getInsets();
                prefSize.width += insets.left + insets.right;
                prefSize.height += insets.top + insets.bottom;
                return prefSize;
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            synchronized (target.getTreeLock()) {
                Dimension minSize = new Dimension(0, 0);
                VisibleComponentsIterator it = new VisibleComponentsIterator(target);
                while (it.hasNext()) {
                    JComponent comp = it.next();
                    Dimension compSize = comp.getMinimumSize();
                    minSize.width = Math.max(minSize.width, compSize.width);
                    minSize.height += compSize.height;
                }
                Insets insets = target.getInsets();
                minSize.width += insets.left + insets.right;
                minSize.height += insets.top + insets.bottom;
                return minSize;
            }
        }

        @Override
        public void layoutContainer(Container target) {
            synchronized (target.getTreeLock()) {
                Insets insets = target.getInsets();
                int x = insets.left, y = insets.top;
                int height = 0;
                int clientWidth = target.getWidth() - insets.right;
                VisibleComponentsIterator it = new VisibleComponentsIterator(target);
                ArrayList<ArrayList<CompInfo>> compInfoRows = new ArrayList<ArrayList<CompInfo>>();
                while (it.hasNext()) {
                    ArrayList<CompInfo> compInfoRow = new ArrayList<CompInfo>();
                    compInfoRows.add(compInfoRow);
                    while (it.hasNext()) {
                        JComponent comp = it.next();
                        CompInfo compInfo = new CompInfo();
                        compInfo.component = comp;
                        Dimension compSize = comp.getPreferredSize();
                        compInfo.bounds.x = x;
                        compInfo.bounds.y = y;
                        compInfo.bounds.width = compSize.width;
                        compInfo.bounds.height = compSize.height;
                        if (!compInfoRow.isEmpty() && (it.isNewLine() || ((compInfo.bounds.x + compInfo.bounds.width) > clientWidth))) {
                            y += height;
                            height = 0;
                            compInfo.bounds.x = x = insets.left;
                            compInfo.bounds.y = y;
                            compInfoRow = new ArrayList<CompInfo>();
                            compInfoRows.add(compInfoRow);
                        }
                        x += compSize.width;
                        height = Math.max(height, compInfo.bounds.height);
                        compInfoRow.add(compInfo);
                    }
                }
                int maxWidth = 0, maxHeight = 0;
                for (ArrayList<CompInfo> compInfoRow : compInfoRows) {
                    CompInfo compInfo = compInfoRow.get(compInfoRow.size() - 1);
                    maxWidth = Math.max(maxWidth, compInfo.bounds.x + compInfo.bounds.width);
                }
                for (CompInfo compInfo : compInfoRows.get(compInfoRows.size() - 1)) maxHeight = Math.max(maxHeight, compInfo.bounds.y + compInfo.bounds.height);
                int deltaX = 0;
                if (maxWidth < target.getWidth()) switch(Options.getEnum(options, CommonOptions.HORIZONTAL_ALIGN_OPTION, HORIZONTAL_ALIGN_DEFAULT)) {
                    case CENTER:
                    case DEFAULT:
                        deltaX = (target.getWidth() - maxWidth) / 2;
                        break;
                    case LEADING:
                    case JUSTIFY:
                        deltaX = 0;
                        break;
                    case TRAILING:
                        deltaX = target.getWidth() - maxWidth;
                        break;
                }
                int deltaY = 0;
                if (maxHeight < target.getHeight()) switch(Options.getEnum(options, CommonOptions.VERTICAL_ALIGN_OPTION, VERTICAL_ALIGN_DEFAULT)) {
                    case MIDDLE:
                    case DEFAULT:
                        deltaY = (target.getHeight() - maxHeight) / 2;
                        break;
                    case TOP:
                        deltaY = 0;
                        break;
                    case BOTTOM:
                    case BASELINE:
                        deltaY = target.getHeight() - maxHeight;
                        break;
                }
                for (ArrayList<CompInfo> compInfoRow : compInfoRows) for (CompInfo compInfo : compInfoRow) {
                    compInfo.bounds.x += deltaX;
                    compInfo.bounds.y += deltaY;
                    compInfo.component.setBounds(compInfo.bounds);
                }
            }
        }
    }

    public SwingChunk(String name, Options options) {
        super(name, options);
    }

    @Override
    protected JComponent createComponent() {
        HQPanel comp = new HQPanel(new ChunkLayout(getOptions()));
        if (DEBUG_BORDERS) comp.setBorder(new LineBorder(Color.RED));
        Insets paddings = (Insets) Options.getObject(getOptions(), CommonOptions.PADDINGS, null);
        if (paddings != null) comp.setBorder(new EmptyBorder(paddings));
        return comp;
    }

    private JPanel getPanel() {
        return (JPanel) getComponent();
    }

    @Override
    public void addParagraph(Paragraph paragraph) {
        JPanel panel = getPanel();
        Insets margins = (Insets) Options.getObject(paragraph.getOptions(), CommonOptions.MARGINS, Options.getObject(getOptions(), PARAGRAPH_MARGINS, MARGINS_DEFAULT));
        Border marginsBorder = null;
        if ((margins != null) && ((margins.left != 0) || (margins.top != 0) || (margins.bottom != 0) || (margins.right != 0))) marginsBorder = new EmptyBorder(margins);
        if (paragraph.getNodes().isEmpty()) {
            JComponent comp = new HQLabel(" ");
            if (marginsBorder != null) comp.setBorder(marginsBorder);
            panel.add(comp);
            comp.putClientProperty(LINE_START_CLIENT_PROPERTY, Boolean.TRUE);
        } else {
            boolean isFirst = true;
            for (Node node : paragraph.getNodes()) {
                JComponent comp;
                if (node instanceof TextNode) comp = new HQLabel(((TextNode) node).getText() + " "); else if (node instanceof PictureNode) comp = new HQLabel(((SwingPicture) ((PictureNode) node).getPicture()).getIcon()); else comp = new HQLabel(node.toString() + " ");
                int textSizePercent = Options.getInt(node.getOptions(), Paragraph.TEXT_SIZE_PERCENT_OPTION, Options.getInt(paragraph.getOptions(), Paragraph.TEXT_SIZE_PERCENT_OPTION, Options.getInt(getOptions(), Paragraph.TEXT_SIZE_PERCENT_OPTION, 100)));
                if (textSizePercent != 100) {
                    Font font = comp.getFont();
                    Font deriveFont = font.deriveFont((font.getSize2D() * textSizePercent) / 100);
                    comp.setFont(deriveFont);
                }
                if (isFirst) {
                    isFirst = false;
                    comp.putClientProperty(LINE_START_CLIENT_PROPERTY, Boolean.TRUE);
                }
                if (DEBUG_BORDERS) comp.setBorder(LineBorder.createBlackLineBorder());
                if (marginsBorder != null) comp.setBorder(marginsBorder);
                panel.add(comp);
            }
        }
    }
}
