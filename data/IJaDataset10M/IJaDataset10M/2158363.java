package akiraviz.view.technique.heiankyoview;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.heiankyoview2.core.placement.Packing;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.tree.Tree;

/**
 * 平安京ビューを表示させる為に十分なデータと、
 * インタラクションに耐えうる振る舞いを実装したデフォルト実装です。
 * これ以外はたぶん必要になりませんっ！
 * 
 * 木を読み込んで、それを伊藤先生の定義したNode, Branch木に変換します。
 * それにライブラリを使って平安京ビューの複雑なアルゴリズムを関数的に施して、
 * そこからリストとして色つきバーや、それを囲う長方形を保持します。
 * 
 * このモデル実装を利用する事で、ビューに描画させます。
 * 
 * @author akira
 *
 */
public class DefaultHeiankyoViewModel implements HeiankyoViewModel {

    private HeiankyoViewableTree _heiankyoViewableTree;

    private int _branchNum;

    private Tree _tree;

    private ArrayList<NodeBranchSet> _nodeBranchSetList;

    private ArrayList<ColoredBar> _coloredBarList;

    private ArrayList<BranchRectangle> _branchRectangleList;

    private NodeBranchSet _rootSet;

    private Point2D.Float _center;

    private float _rate;

    private HeiankyoViewableFactor _heiankyoViewableFactor;

    public DefaultHeiankyoViewModel(HeiankyoViewableTree heiankyoViewableTree, HeiankyoViewableFactor factor) {
        _heiankyoViewableTree = heiankyoViewableTree;
        _branchNum = 0;
        _nodeBranchSetList = new ArrayList<NodeBranchSet>();
        _coloredBarList = new ArrayList<ColoredBar>();
        _branchRectangleList = new ArrayList<BranchRectangle>();
        _heiankyoViewableFactor = factor;
        _tree = new Tree();
    }

    public void createHeiankyoView() {
        generateTree();
        packNodes();
        setShapeLists();
        setNormalizeTransformFactors();
        transformBothLists();
        setColorsToBars();
        setAttitudeToBars();
    }

    /**
	 * Node, Branch実装の木にアルゴリズムを関数的に施して、
	 * 返ってきた木を保存します。
	 */
    void packNodes() {
        _tree = new Packing().placeNodesAllBranch(_tree);
    }

    /**
	 * １つのノードについてセットを生成する。
	 * ぬるの場合は、ブランチを明示的にぬるにする。
	 * 
	 * コンストタクタで初期化してあるNode, Branch木に対して、
	 * ブランチを所属させる。
	 * 
	 * @param node
	 * @return
	 */
    NodeBranchSet generateNodeBranchSet(Object node) {
        if (_heiankyoViewableTree.isLeaf(node)) {
            NodeBranchSet set = new NodeBranchSet(new Node(0, null), null);
            set._node = node;
            return set;
        } else {
            NodeBranchSet set = new NodeBranchSet(new Node(0, null), new Branch(++_branchNum, _tree));
            set._node = node;
            return set;
        }
    }

    /**
	 * HeiankyoViewable中のあるnode以下の
	 * サブツリーをNodeBranchSetを用いて形成（その中身はNodeとBranchのチェインである）する。
	 * 
	 * @param node
	 * @return そのサブツリーのルートノードブランチセット
	 */
    NodeBranchSet generateSubTree(Object node) {
        NodeBranchSet parentSet = generateNodeBranchSet(node);
        _nodeBranchSetList.add(parentSet);
        for (Object child : _heiankyoViewableTree.getChildren(node)) {
            parentSet.addChildSet(generateSubTree(child));
        }
        return parentSet;
    }

    /**
	 * _treeというフィールドに、_heiankyoViewableTree
	 * からTreeオブジェクトを生成して、格納する。
	 * 
	 */
    void generateTree() {
        Object root = _heiankyoViewableTree.getRoot();
        Branch rootBranch = new Branch(++_branchNum, _tree);
        _rootSet = new NodeBranchSet(_tree.getRootNode(), rootBranch);
        _rootSet._node = root;
        _nodeBranchSetList.add(_rootSet);
        _tree.setRootBranch(rootBranch);
        for (Object child : _heiankyoViewableTree.getChildren(root)) {
            NodeBranchSet subTreeRootSet = generateSubTree(child);
            _rootSet.addChildSet(subTreeRootSet);
        }
        System.out.println(_nodeBranchSetList.size());
    }

    /**
	 * 高さを決定します。
	 */
    void setAttitudeToBars() {
        for (ColoredBar bar : _coloredBarList) {
            bar._attitude = _heiankyoViewableTree.getProperty(bar._node, _heiankyoViewableFactor.getAttitudeFactorKey());
        }
        float maxAttitude = Float.MIN_VALUE;
        float minAttitude = Float.MAX_VALUE;
        for (ColoredBar bar : _coloredBarList) {
            if (bar._attitude > maxAttitude) {
                maxAttitude = bar._attitude;
            }
            if (bar._attitude < minAttitude) {
                minAttitude = bar._attitude;
            }
        }
        for (ColoredBar bar : _coloredBarList) {
            bar._attitude = (bar._attitude - minAttitude) / (maxAttitude - minAttitude);
        }
    }

    /**
	 * 色を決定します。	
	 */
    void setColorsToBars() {
        for (ColoredBar bar : _coloredBarList) {
            bar._colorFactor = _heiankyoViewableTree.getProperty(bar._node, _heiankyoViewableFactor.getColorFactorKey());
        }
        float maxColorFactor = Float.MIN_VALUE;
        float minColorFactor = Float.MAX_VALUE;
        for (ColoredBar bar : _coloredBarList) {
            if (bar._colorFactor > maxColorFactor) {
                maxColorFactor = bar._colorFactor;
            }
            if (bar._colorFactor < minColorFactor) {
                minColorFactor = bar._colorFactor;
            }
        }
        for (ColoredBar bar : _coloredBarList) {
            float hue = (bar._colorFactor - minColorFactor) / (maxColorFactor - minColorFactor);
            bar._color = Color.getHSBColor(hue, 0.5F, 0.7F);
        }
    }

    /**
	 * 直方体のリストを正規化して、
	 * 高さや色を決める為のファクタをセットします。
	 * @param factor
	 */
    public void setHeiankyoViewableFactor(HeiankyoViewableFactor factor) {
        _heiankyoViewableFactor = factor;
    }

    /**
	 * 大枠の長方形、
	 * つまりルートセットの長方形の中心と
	 * 幅、縦のうち大きい方が1.2になるように
	 * なる比を格納する。
	 * 
	 * この中心座標（ベクター）と、比を利用して、
	 * 座標変換を行う。
	 */
    void setNormalizeTransformFactors() {
        float centerX = _rootSet.getX() + _rootSet.getWidth() / 2.0F;
        float centerY = _rootSet.getY() - _rootSet.getHeight() / 2.0F;
        _center = new Point2D.Float(centerX, centerY);
        float minLength = _rootSet.getWidth() < _rootSet.getHeight() ? _rootSet.getHeight() : _rootSet.getWidth();
        _rate = minLength / 2.0F;
    }

    /**
	 * 葉なら、直方体、
	 * ブランチなら長方形という用にリストに格納する。
	 * 直方体の初期高さは0とする。
	 * 
	 * 別に分ける必要もないのかも知れませんが、
	 * 明示的に分けておいた方が分かりやすいかなと思いました。
	 * 処理の速さとかは、たぶんそんなに変わらないかと思います。
	 * 
	 * ここで得られたリストはデータ宝石箱を描くだけに
	 * 十分なデータを保持しています（そしてそれに座標変換を施せば任意の画面に収まるように出来ます）。
	 * 高さや色をつける事が必要になりますが、それはここではやりません。
	 */
    void setShapeLists() {
        for (NodeBranchSet branchSet : _nodeBranchSetList) {
            if (branchSet.isLeafSet()) {
                ColoredBar bar = new ColoredBar();
                bar._x = branchSet.getX();
                bar._y = branchSet.getY();
                bar._width = branchSet.getWidth();
                bar._height = branchSet.getHeight();
                bar._node = branchSet._node;
                _coloredBarList.add(bar);
            } else {
                BranchRectangle branchRectangle = new BranchRectangle();
                branchRectangle._x = branchSet.getX();
                branchRectangle._y = branchSet.getY();
                branchRectangle._width = branchSet.getWidth();
                branchRectangle._height = branchSet.getHeight();
                _branchRectangleList.add(branchRectangle);
            }
        }
    }

    /**
	 * 図形を保有しているリストのすべてに
	 * 座標変換を施す。
	 * _centerに保存されているベクトルの逆ベクトルをかけます。
	 * 
	 * 全体が座標上で0,0まわり[-1, 1]に集まるように変換します。
	 */
    void transformBothLists() {
        for (ColoredBar cuboid : _coloredBarList) {
            Point2D.Float vector = new Point2D.Float(-_center.x, -_center.y);
            cuboid.transform(vector, _rate);
        }
        for (BranchRectangle rectangle : _branchRectangleList) {
            Point2D.Float vector = new Point2D.Float(-_center.x, -_center.y);
            rectangle.transform(vector, _rate);
        }
    }

    @Override
    public BranchRectangle[] getBranchRectangles() {
        return _branchRectangleList.toArray(new BranchRectangle[_branchRectangleList.size()]);
    }

    @Override
    public ColoredBar[] getColoredBars() {
        return _coloredBarList.toArray(new ColoredBar[_coloredBarList.size()]);
    }
}
