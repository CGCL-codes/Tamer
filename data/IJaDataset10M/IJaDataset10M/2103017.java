package domain;

import gfx.*;
import java.util.*;
import domain.BranchInformation.Direction;

/**
 * @author dmutti@gmail.com
 */
public class BranchDistribution {

    private final RelativePoint treePoint;

    private final ClassDescription desc;

    private int offset;

    private int maxBranchSize;

    private int height;

    private static final Direction[] top = { Direction.LEFT_HORIZONTAL, Direction.LEFT_DIAGONAL, Direction.UP_LEFT, Direction.UP_RIGHT, Direction.RIGHT_DIAGONAL, Direction.RIGHT_HORIZONTAL };

    public BranchDistribution(ClassDescription desc, RelativePoint treePoint, int offset) {
        this.desc = desc;
        this.treePoint = treePoint;
        this.offset = offset;
        this.maxBranchSize = (Math.max(desc.getNotEmptyMethodsCollectionSize() - top.length, offset) + offset) / 2;
        this.height = Math.max(leftSide(), rightSide()) + offset;
    }

    public List<BranchInformation> getResult() {
        int methodIdx = 0;
        int heightInc = 0;
        List<BranchInformation> result = new ArrayList<BranchInformation>();
        while (methodIdx < leftSide() && methodIdx < desc.getNotEmptyMethodsCollectionSize()) {
            result.add(new BranchInformation(desc.getNotEmptyMethods().get(methodIdx++), Direction.LEFT_HORIZONTAL, new RelativePoint(treePoint.getX(), treePoint.getY() + offset + heightInc++), maxBranchSize));
        }
        int topIdx = 0;
        while (topIdx < top.length && methodIdx < desc.getNotEmptyMethodsCollectionSize()) {
            result.add(new BranchInformation(desc.getNotEmptyMethods().get(methodIdx++), top[topIdx++], new RelativePoint(treePoint.getX(), treePoint.getY() + offset + heightInc), maxBranchSize));
        }
        while (methodIdx < desc.getNotEmptyMethodsCollectionSize()) {
            result.add(new BranchInformation(desc.getNotEmptyMethods().get(methodIdx), Direction.RIGHT_HORIZONTAL, new RelativePoint(treePoint.getX(), treePoint.getY() + offset + --heightInc), maxBranchSize));
            methodIdx++;
        }
        return result;
    }

    private int leftSide() {
        return (int) Math.ceil((double) (desc.getNotEmptyMethodsCollectionSize() - top.length) / 2);
    }

    private int rightSide() {
        return Math.max(desc.getNotEmptyMethodsCollectionSize() - leftSide() - top.length, 0);
    }

    public int getHeight() {
        return height;
    }
}
