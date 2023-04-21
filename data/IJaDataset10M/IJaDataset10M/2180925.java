package jpredmethodtreeview;

abstract class AbstractDrawableTreeNode<T extends AbstractDrawableTreeNode<T, D>, D> extends AbstractTreeNode<T, D> {

    AbstractDrawableTreeNode(D data, IAbstractTreeNodeFactory<T, D> nodeFactory) {
        super(data, nodeFactory);
    }
}
