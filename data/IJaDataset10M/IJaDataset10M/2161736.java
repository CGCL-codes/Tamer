package Presentation;

public abstract class FakeView implements Iview {

    public String m_Right;

    public String m_Left;

    public String m_pivot;

    public String m_helperArray;

    public IActionHandler m_SortedArray;

    public IActionHandler m_Merge;

    public IActionHandler m_mergeSort;

    public IActionHandler m_quickSort;

    public IActionHandler m_MergeSortHandler;

    public IActionHandler m_QuickSortHandler;

    public String getRight() {
        return m_Right;
    }

    public String getLeft() {
        return m_Left;
    }

    @Override
    public String getPivot() {
        return m_pivot;
    }

    @Override
    public String sethelperArray() {
        return m_helperArray;
    }

    @Override
    public void setMerge(IActionHandler handler) {
        m_Merge = handler;
    }

    @Override
    public void setSortedArray(IActionHandler handler) {
        m_SortedArray = handler;
    }

    @Override
    public void setMergeSortHandler(IActionHandler handler) {
        m_MergeSortHandler = handler;
    }

    @Override
    public void setQuickSortHandler(IActionHandler handler) {
        m_QuickSortHandler = handler;
    }
}
