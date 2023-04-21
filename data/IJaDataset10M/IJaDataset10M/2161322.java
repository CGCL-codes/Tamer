package _bye_util.logintrec;

/**
 * 
 * @author kagioglu
 */
public interface TreeNode {

    int size();

    int depth();

    TreeNode newInt(int lowerBound, int upperBound, Ref newInt);

    TreeNode recycleInt(int value);

    Branch shortenLeft(Branch parent);

    Branch shortenRight(Branch parent);
}
