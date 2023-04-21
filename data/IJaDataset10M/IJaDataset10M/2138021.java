package org.skeegenin.searKit;

public interface IKnownDepthSearchNode<TSearchState extends ISearchState<?, TSearchState>> extends ISearchNode<TSearchState> {

    public int getSearchNodeDepth();
}
