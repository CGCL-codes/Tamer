package akiraviz.view.technique.treemap2d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import akiraviz.view.infotable.InfoNode;

/**
 * たぶん別に抽象とかする必要もなく、
 * 木の段階で抽象にしておけば、このビューはこの実装クラスに依存してもいいと思うのですが、
 * DIPを守る為にあえてそうしました。完全に必要ない抽象だと今は反省しています。
 * でも書き換える気はあまりありません。複雑になれば書き換えます。
 * 
 * @author akira
 *
 */
public class DefaultTreeMap2DModel implements TreeMap2DModel {

    private LinkedList<ColoredRectangle> _coloredRectangleList;

    private TreeMappable2D _treeMappable2D;

    private LinkedList<TreeMapModelChangedListener> _treeMapModelChangedListeners;

    private ColoredRectangle _formerBrightRectangle;

    private Color _formerBrightColor;

    private TreeMap2DFactor _treeMap2DFactor;

    public DefaultTreeMap2DModel(TreeMappable2D treeMappable, TreeMap2DFactor treeMap2DFactor) {
        _coloredRectangleList = new LinkedList<ColoredRectangle>();
        _treeMapModelChangedListeners = new LinkedList<TreeMapModelChangedListener>();
        _treeMappable2D = treeMappable;
        _formerBrightRectangle = null;
        _treeMap2DFactor = treeMap2DFactor;
    }

    /**
	 * ツリーマップを作成します。
	 * ツリーマップは内部に保持します。
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
    public final void create(float x, float y, float width, float height) {
        _coloredRectangleList.clear();
        Object root = _treeMappable2D.getRoot();
        devideRectangleForChildren(root, x, y, width, height, 0);
        setColorsOnRectangles();
    }

    /**
	 * 親にわりあてられた長方形を子に分配します。
	 * それを再帰的に行います。
	 * @param node 親
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param depth
	 */
    final void devideRectangleForChildren(Object node, float x, float y, float width, float height, int depth) {
        if (_treeMappable2D.isLeaf(node)) {
            addColoredRectangleToList(node, x, y, width, height);
        } else {
            divideNodeRectangleByDepth(node, x, y, width, height, depth);
        }
    }

    void addColoredRectangleToList(Object node, float x, float y, float width, float height) {
        ColoredRectangle coloredRectangle = new ColoredRectangle();
        coloredRectangle._node = node;
        coloredRectangle._id = _treeMappable2D.getID(node);
        coloredRectangle._x = x;
        coloredRectangle._y = y;
        coloredRectangle._width = width;
        coloredRectangle._height = height;
        _coloredRectangleList.add(coloredRectangle);
    }

    void divideNodeRectangleByDepth(Object node, float x, float y, float width, float height, int depth) {
        float totalSize = 0F;
        for (Object child : _treeMappable2D.getChildren(node)) {
            totalSize += _treeMappable2D.getProperty(child, _treeMap2DFactor.getSizeFactorKey());
        }
        if (totalSize == 0) {
            totalSize = Float.MAX_VALUE;
        }
        float alreadyAllocated = 0F;
        switch(depth % 2) {
            case 0:
                for (Object child : _treeMappable2D.getChildren(node)) {
                    float ratio = _treeMappable2D.getProperty(child, _treeMap2DFactor.getSizeFactorKey()) / totalSize;
                    assert (totalSize > 0);
                    float newlyAllocated = width * ratio;
                    float newX = x + alreadyAllocated;
                    float newY = y;
                    float newWidth = newlyAllocated;
                    float newHeight = height;
                    alreadyAllocated += newlyAllocated;
                    int newDepth = depth + 1;
                    devideRectangleForChildren(child, newX, newY, newWidth, newHeight, newDepth);
                }
                break;
            case 1:
                for (Object child : _treeMappable2D.getChildren(node)) {
                    float ratio = _treeMappable2D.getProperty(child, _treeMap2DFactor.getSizeFactorKey()) / totalSize;
                    float newlyAllocated = height * ratio;
                    float newX = x;
                    float newY = y + alreadyAllocated;
                    float newWidth = width;
                    float newHeight = newlyAllocated;
                    alreadyAllocated += newlyAllocated;
                    int newDepth = depth + 1;
                    devideRectangleForChildren(child, newX, newY, newWidth, newHeight, newDepth);
                }
                break;
        }
    }

    @Override
    public void fireDataChangeToTreeModelChangedListener() {
        for (TreeMapModelChangedListener listener : _treeMapModelChangedListeners) {
            listener.update();
        }
    }

    @Override
    public Color getColor(Object rect) {
        return ((ColoredRectangle) rect)._color;
    }

    @Override
    public float getHeight(Object rect) {
        return ((ColoredRectangle) rect)._height;
    }

    @Override
    public String getID(Object rect) {
        return ((ColoredRectangle) rect)._id;
    }

    @Override
    public InfoNode getInfoNode(Object rect) {
        return _treeMappable2D.getInfoNode(((ColoredRectangle) rect)._node);
    }

    @Override
    public Object getRectangle(int x, int y) {
        ColoredRectangle tmp = null;
        for (ColoredRectangle rect : _coloredRectangleList) {
            if (rect.contains(x, y)) {
                tmp = rect;
            }
        }
        return tmp;
    }

    @Override
    public Object getRectangleByID(String id) {
        ColoredRectangle tmp = null;
        for (ColoredRectangle rect : _coloredRectangleList) {
            if (rect.hasID(id)) {
                tmp = rect;
            }
        }
        return tmp;
    }

    @Override
    public Object[] getRectangles() {
        return _coloredRectangleList.toArray(new Object[_coloredRectangleList.size()]);
    }

    @Override
    public float getWidth(Object rect) {
        return ((ColoredRectangle) rect)._width;
    }

    @Override
    public float getX(Object rect) {
        return ((ColoredRectangle) rect)._x;
    }

    @Override
    public float getY(Object rect) {
        return ((ColoredRectangle) rect)._y;
    }

    @Override
    public void registerTreeModelChangedListener(TreeMapModelChangedListener listener) {
        _treeMapModelChangedListeners.add(listener);
    }

    @Override
    public void setBrighterColor(Object rect) {
        if (_formerBrightRectangle != null) {
            _formerBrightRectangle._color = _formerBrightColor;
        }
        ColoredRectangle tmp = (ColoredRectangle) rect;
        _formerBrightColor = tmp._color;
        tmp._color = _formerBrightColor.brighter();
        _formerBrightRectangle = tmp;
    }

    /**
	 * 色ファクタを利用して正規化して、
	 * 長方形の色を決定します。
	 * ファクタを決定するキーをセットしてから、
	 * カラーだけ変える事も出来ます。
	 */
    public void setColorsOnRectangles() {
        for (ColoredRectangle coloredRectangle : _coloredRectangleList) {
            coloredRectangle._colorFactor = coloredRectangle._colorFactor = _treeMappable2D.getProperty(coloredRectangle._node, _treeMap2DFactor.getColorFactorKey());
        }
        float maxColorFactor = Float.MIN_VALUE;
        float minColorFactor = Float.MAX_VALUE;
        for (ColoredRectangle rectangle : _coloredRectangleList) {
            if (rectangle._colorFactor > maxColorFactor) {
                maxColorFactor = rectangle._colorFactor;
            }
            if (rectangle._colorFactor < minColorFactor) {
                minColorFactor = rectangle._colorFactor;
            }
        }
        for (ColoredRectangle rectangle : _coloredRectangleList) {
            float hue = (rectangle._colorFactor - minColorFactor) / (maxColorFactor - minColorFactor);
            rectangle._color = Color.getHSBColor(hue, 0.5F, 0.7F);
        }
    }

    @Override
    public void setTreeMap2DFactor(TreeMap2DFactor factor) {
        _treeMap2DFactor = factor;
    }
}
