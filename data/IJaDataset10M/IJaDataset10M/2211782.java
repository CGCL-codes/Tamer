package net.sf.joafip.btreeplus.entity;

import java.util.Arrays;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.kvstore.service.HeapException;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class NonTerminalPage extends AbstractElement implements INonTerminalPage {

    public static int HEAD_TAIL_SIZE = 8 + 1 + 4 + 4 + 8;

    private long[] pagePositions;

    private DataRecordIdentifier[] keys;

    private int numberOfKeyEntries;

    private int byteSize;

    private IPageRecordable parentPage;

    private int inParentIndex;

    private DataRecordIdentifier middleKey;

    public NonTerminalPage() {
        this(0);
        updateByteSize();
    }

    public NonTerminalPage(final int numberOfKeyEntries) {
        super();
        this.numberOfKeyEntries = numberOfKeyEntries;
        keys = new DataRecordIdentifier[numberOfKeyEntries];
        pagePositions = new long[numberOfKeyEntries + 1];
        byteSize = 0;
    }

    public NonTerminalPage(final int numberOfKeyEntries, final DataRecordIdentifier[] keys, final long[] pagePosition) {
        super();
        assert numberOfKeyEntries == keys.length && numberOfKeyEntries + 1 == pagePosition.length;
        this.numberOfKeyEntries = numberOfKeyEntries;
        this.keys = keys;
        this.pagePositions = pagePosition;
        updateByteSize();
    }

    @Override
    public EnumRecordType getRecordType() {
        return EnumRecordType.NON_TERMINAL_PAGE;
    }

    @Override
    public int getNumberOfPage() {
        return 1;
    }

    @Override
    public void setParentPage(final IPageRecordable parentPage, final int inParentIndex) {
        this.parentPage = parentPage;
        this.inParentIndex = inParentIndex;
    }

    @Override
    public IPageRecordable getParentPage() {
        return parentPage;
    }

    @Override
    public int getInParentIndex() throws HeapException {
        return inParentIndex;
    }

    public void setEntry(final int index, final long pagePointer, final DataRecordIdentifier key) throws HeapException {
        pagePositions[index] = pagePointer;
        if (key != null) {
            keys[index] = key;
        }
        setValueIsChangedValueToSave();
    }

    /**
	 * change key
	 * 
	 * @param index
	 * @param key
	 * @return true if not need to split (not too big)
	 * @throws HeapException
	 */
    public boolean setKey(final int index, final DataRecordIdentifier key) throws HeapException {
        DataRecordIdentifier oldKey = keys[index];
        keys[index] = key;
        byteSize = byteSize - entrySize(oldKey) + entrySize(key);
        assert byteSize == computeByteSize();
        setValueIsChangedValueToSave();
        return byteSize < PageConstant.PAGE_SIZE;
    }

    @Override
    public int getByteSize() {
        assert byteSize != 0 : "unknow current byte size";
        return byteSize;
    }

    @Override
    public void updateByteSize() {
        byteSize = computeByteSize();
    }

    private int computeByteSize() {
        int xbyteSize = HEAD_TAIL_SIZE;
        for (int index = 0; index < numberOfKeyEntries; index++) {
            xbyteSize += entrySize(keys[index]);
        }
        return xbyteSize;
    }

    @Override
    public int getNumberOfKeyEntries() {
        return numberOfKeyEntries;
    }

    @Override
    public long getPagePointer(final int index) {
        return pagePositions[index];
    }

    @Override
    public DataRecordIdentifier getKey(final int index) {
        return keys[index];
    }

    /**
	 * 
	 * @param dataRecordIdentifier
	 * @return sub page index for data record identifier
	 */
    public int getIndex(final DataRecordIdentifier dataRecordIdentifier) {
        int index;
        for (index = 0; index < numberOfKeyEntries; index++) {
            if (keys[index].compareTo(dataRecordIdentifier) >= 0) {
                return index;
            }
        }
        return index;
    }

    /**
	 * 
	 * @param existingLeftILeafPage
	 * @param newRightILeafPage
	 * @return true if add not need split
	 * @throws HeapException
	 */
    public boolean add(final ILeafPage existingLeftILeafPage, final ILeafPage newRightILeafPage) throws HeapException {
        final int index = existingLeftILeafPage.getInParentIndex();
        final DataRecordIdentifier key = existingLeftILeafPage.getLastKey();
        final int entrySize = entrySize(key);
        final int afterLength = numberOfKeyEntries - index;
        numberOfKeyEntries++;
        DataRecordIdentifier[] newKeys = new DataRecordIdentifier[numberOfKeyEntries];
        long[] newPagePosition = new long[numberOfKeyEntries + 1];
        if (index != 0) {
            System.arraycopy(keys, 0, newKeys, 0, index);
        }
        System.arraycopy(pagePositions, 0, newPagePosition, 0, index + 1);
        newKeys[index] = key;
        newPagePosition[index + 1] = newRightILeafPage.getPositionInFile();
        System.arraycopy(pagePositions, index + 1, newPagePosition, index + 2, afterLength);
        if (afterLength != 0) {
            System.arraycopy(keys, index, newKeys, index + 1, afterLength);
        }
        keys = newKeys;
        pagePositions = newPagePosition;
        assert byteSize != 0 : "unknow current byte size";
        byteSize += entrySize;
        assert byteSize == computeByteSize();
        setValueIsChangedValueToSave();
        return byteSize < PageConstant.PAGE_SIZE;
    }

    /**
	 * 
	 * @param leftSonNonTerminalPage
	 * @param middleKey
	 * @param rightSonNonTerminalPage
	 * @return true if add not need split
	 * @throws HeapException
	 */
    public boolean add(final INonTerminalPage leftSonNonTerminalPage, final DataRecordIdentifier middleKey, final INonTerminalPage rightSonNonTerminalPage) throws HeapException {
        final int index = leftSonNonTerminalPage.getInParentIndex();
        final int entrySize = entrySize(middleKey);
        final int afterLength = numberOfKeyEntries - index;
        numberOfKeyEntries++;
        DataRecordIdentifier[] newKeys = new DataRecordIdentifier[numberOfKeyEntries];
        long[] newPagePositions = new long[numberOfKeyEntries + 1];
        if (index != 0) {
            System.arraycopy(keys, 0, newKeys, 0, index);
        }
        System.arraycopy(pagePositions, 0, newPagePositions, 0, index + 1);
        newKeys[index] = middleKey;
        newPagePositions[index + 1] = rightSonNonTerminalPage.getPositionInFile();
        if (afterLength != 0) {
            System.arraycopy(keys, index, newKeys, index + 1, afterLength);
            System.arraycopy(pagePositions, index + 1, newPagePositions, index + 2, afterLength);
        }
        keys = newKeys;
        pagePositions = newPagePositions;
        assert byteSize != 0 : "unknow current byte size";
        byteSize += entrySize;
        assert byteSize == computeByteSize();
        setValueIsChangedValueToSave();
        return byteSize < PageConstant.PAGE_SIZE;
    }

    public INonTerminalPage split() throws HeapException {
        final int splitIndex = numberOfKeyEntries / 2;
        middleKey = keys[splitIndex];
        final int newNumberOfKeyEntries = splitIndex;
        final int newNonTerminalPageNumberOfKeyEntries = numberOfKeyEntries - (splitIndex + 1);
        final DataRecordIdentifier[] newKeys = Arrays.copyOf(keys, newNumberOfKeyEntries);
        final long[] newPagePosition = Arrays.copyOf(pagePositions, newNumberOfKeyEntries + 1);
        final DataRecordIdentifier[] newNonTerminalKeys = Arrays.copyOfRange(keys, splitIndex + 1, numberOfKeyEntries);
        final long[] newNonTerminalPagePosition = Arrays.copyOfRange(pagePositions, splitIndex + 1, numberOfKeyEntries + 1);
        final INonTerminalPage nonTerminalPage = new NonTerminalPage(newNonTerminalPageNumberOfKeyEntries, newNonTerminalKeys, newNonTerminalPagePosition);
        keys = newKeys;
        pagePositions = newPagePosition;
        numberOfKeyEntries = newNumberOfKeyEntries;
        updateByteSize();
        setValueIsChangedValueToSave();
        return nonTerminalPage;
    }

    /**
	 * get and clear middle key computed by {@link #split()}
	 * 
	 * @return middle key computed by {@link #split()}
	 */
    public DataRecordIdentifier getAndClearMiddleKey() {
        final DataRecordIdentifier result = middleKey;
        middleKey = null;
        return result;
    }

    public void remove(int leafPageInParentIndex) {
        final DataRecordIdentifier key = keys[leafPageInParentIndex];
        numberOfKeyEntries--;
        DataRecordIdentifier[] newKeys = new DataRecordIdentifier[numberOfKeyEntries];
        long[] newPagePositions = new long[numberOfKeyEntries + 1];
        if (leafPageInParentIndex != 0) {
            System.arraycopy(keys, 0, newKeys, 0, leafPageInParentIndex);
            System.arraycopy(pagePositions, 0, newPagePositions, 0, leafPageInParentIndex);
        }
        final int length = numberOfKeyEntries - leafPageInParentIndex;
        if (length != 0) {
            System.arraycopy(keys, leafPageInParentIndex + 1, newKeys, leafPageInParentIndex, length);
        }
        System.arraycopy(pagePositions, leafPageInParentIndex + 1, newPagePositions, leafPageInParentIndex, length + 1);
        keys = newKeys;
        pagePositions = newPagePositions;
        assert byteSize != 0 : "unknow current byte size";
        byteSize -= entrySize(key);
        assert byteSize == computeByteSize() : "byteSize=" + byteSize + ", computed=" + computeByteSize();
    }

    public boolean wellFilled() {
        assert byteSize != 0 : "unknow current byte size";
        return byteSize >= (PageConstant.PAGE_SIZE >> 1);
    }

    /**
	 * update {@link this#middleKey} if do not merge
	 * 
	 * @param middleKey
	 * @param rightNonTerminalPage
	 * @return true if merge in this page, nonTerminalPage is to be destroyed
	 */
    public boolean equilibrate(final DataRecordIdentifier middleKey, final NonTerminalPage rightNonTerminalPage) {
        assert byteSize != 0 : "unknow current byte size";
        final int rightPageByteSize = rightNonTerminalPage.getByteSize();
        final int middleKeySize = entrySize(middleKey);
        final int rightPageNumberOfKeyEntries = rightNonTerminalPage.numberOfKeyEntries;
        final int totalNumberOfEntries = numberOfKeyEntries + rightPageNumberOfKeyEntries;
        final boolean merged;
        if (byteSize + middleKeySize + rightPageByteSize - HEAD_TAIL_SIZE <= PageConstant.PAGE_SIZE) {
            merged = true;
            pagePositions = Arrays.copyOf(pagePositions, totalNumberOfEntries + 1 + 1);
            keys = Arrays.copyOf(keys, totalNumberOfEntries + 1);
            System.arraycopy(rightNonTerminalPage.pagePositions, 0, pagePositions, numberOfKeyEntries + 1, rightPageNumberOfKeyEntries + 1);
            keys[numberOfKeyEntries] = middleKey;
            System.arraycopy(rightNonTerminalPage.keys, 0, keys, numberOfKeyEntries + 1, rightPageNumberOfKeyEntries);
            numberOfKeyEntries = totalNumberOfEntries + 1;
            byteSize += middleKeySize + rightPageByteSize - HEAD_TAIL_SIZE;
            assert byteSize == computeByteSize() : "byteSize=" + byteSize + ", computed=" + computeByteSize();
        } else {
            merged = false;
            final int newNumberOfEntries = totalNumberOfEntries / 2;
            final int newRightPageNumberOfEntries = totalNumberOfEntries - newNumberOfEntries;
            if (newNumberOfEntries > numberOfKeyEntries) {
                int length = newNumberOfEntries - numberOfKeyEntries;
                pagePositions = Arrays.copyOf(pagePositions, newNumberOfEntries + 1);
                System.arraycopy(rightNonTerminalPage.pagePositions, 0, pagePositions, numberOfKeyEntries + 1, length);
                keys = Arrays.copyOf(keys, newNumberOfEntries);
                keys[numberOfKeyEntries] = middleKey;
                System.arraycopy(rightNonTerminalPage.keys, 0, keys, numberOfKeyEntries + 1, length - 1);
                this.middleKey = rightNonTerminalPage.keys[length - 1];
                numberOfKeyEntries = newNumberOfEntries;
                updateByteSize();
                length = rightPageNumberOfKeyEntries - newRightPageNumberOfEntries;
                rightNonTerminalPage.pagePositions = Arrays.copyOfRange(rightNonTerminalPage.pagePositions, length, rightPageNumberOfKeyEntries + 1);
                rightNonTerminalPage.keys = Arrays.copyOfRange(rightNonTerminalPage.keys, length, rightPageNumberOfKeyEntries);
                rightNonTerminalPage.numberOfKeyEntries = newRightPageNumberOfEntries;
                rightNonTerminalPage.updateByteSize();
            } else if (newRightPageNumberOfEntries > rightPageNumberOfKeyEntries) {
                final int length = newRightPageNumberOfEntries - rightPageNumberOfKeyEntries;
                long[] newPagePositions = Arrays.copyOf(pagePositions, newNumberOfEntries + 1);
                DataRecordIdentifier[] newKeys = Arrays.copyOf(keys, newNumberOfEntries);
                this.middleKey = keys[newNumberOfEntries];
                long[] newRightPagePositions = new long[newRightPageNumberOfEntries + 1];
                DataRecordIdentifier[] newRightKeys = new DataRecordIdentifier[newRightPageNumberOfEntries];
                System.arraycopy(pagePositions, newNumberOfEntries + 1, newRightPagePositions, 0, length);
                System.arraycopy(keys, newNumberOfEntries + 1, newRightKeys, 0, length - 1);
                newRightKeys[length - 1] = middleKey;
                System.arraycopy(rightNonTerminalPage.pagePositions, 0, newRightPagePositions, length, rightPageNumberOfKeyEntries + 1);
                System.arraycopy(rightNonTerminalPage.keys, 0, newRightKeys, length, rightPageNumberOfKeyEntries);
                rightNonTerminalPage.numberOfKeyEntries = newRightPageNumberOfEntries;
                rightNonTerminalPage.keys = newRightKeys;
                rightNonTerminalPage.pagePositions = newRightPagePositions;
                rightNonTerminalPage.updateByteSize();
                numberOfKeyEntries = newNumberOfEntries;
                keys = newKeys;
                pagePositions = newPagePositions;
                updateByteSize();
            }
        }
        return merged;
    }

    public DataRecordIdentifier getLastKey() {
        return keys[numberOfKeyEntries - 1];
    }

    public long getLastPagePosition() {
        return pagePositions[numberOfKeyEntries];
    }
}
