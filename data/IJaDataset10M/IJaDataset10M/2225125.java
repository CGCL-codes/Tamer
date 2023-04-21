package de.grogra.vecmath.geom;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class DefaultCellIterator implements CellIterator, Cloneable {

    private Line line;

    private Octree tree = null;

    private Point3d min;

    private Point3d max;

    private Vector3d eps = new Vector3d();

    private BoundingBox bounds;

    private Vector3d coordToGrid;

    private int enteredFace;

    private Octree.Cell lastCell;

    private Octree.Cell nextCell;

    private double enteringParameter;

    private boolean done;

    private IntersectionList list = new IntersectionList();

    private Vector3d tmpVector = new Vector3d();

    private BoundingBox cellBounds = new BoundingBox(new Point3d(), new Point3d());

    public CellIterator dup() {
        try {
            DefaultCellIterator i = (DefaultCellIterator) clone();
            i.list = new IntersectionList();
            i.tmpVector = new Vector3d();
            i.cellBounds = new BoundingBox(new Point3d(), new Point3d());
            return i;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public void initialize(Octree tree) {
        this.tree = tree;
        min = tree.min;
        max = tree.max;
        eps.scale(0.1, tree.minCellSize);
        bounds = new BoundingBox(min, max);
        coordToGrid = new Vector3d(1 / tree.minCellSize.x, 1 / tree.minCellSize.y, 1 / tree.minCellSize.z);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        if (done) {
            return false;
        }
        if (nextCell != null) {
            return true;
        }
        boolean b = hasNextImpl();
        done = !b;
        return b;
    }

    protected boolean hasNextImpl() {
        double lineStart = line.start;
        double lineEnd = line.end;
        line.start = -Double.MAX_VALUE;
        line.end = Double.MAX_VALUE;
        list.clear();
        lastCell.getExtent(tree, cellBounds.min, cellBounds.max);
        BoundingBox.computeIntersections(cellBounds, cellBounds.min, cellBounds.max, line, enteredFace < 0, list, enteredFace, -1);
        line.start = lineStart;
        line.end = lineEnd;
        if (list.size == 0) {
            return false;
        }
        if (enteredFace < 0) {
            list.remove(0, 1);
        }
        if (list.size != 1) {
            throw new RuntimeException("list.size = " + list.size);
        }
        Intersection is = list.elements[0];
        if (is.type == Intersection.ENTERING) {
            throw new RuntimeException("cell entered: " + is);
        }
        tmpVector.set(is.getPoint());
        switch(is.face) {
            case Cube.FRONT:
                nextCell = lastCell.front;
                if (nextCell == null) {
                    return false;
                } else if (nextCell.children == null) {
                    break;
                }
                tmpVector.y -= eps.y;
                if (tmpVector.y < min.y) {
                    return false;
                }
                nextCell = getCellFromPoint(nextCell, tmpVector);
                break;
            case Cube.BACK:
                nextCell = lastCell.back;
                if (nextCell == null) {
                    return false;
                } else if (nextCell.children == null) {
                    break;
                }
                tmpVector.y += eps.y;
                if (tmpVector.y > max.y) {
                    return false;
                }
                nextCell = getCellFromPoint(nextCell, tmpVector);
                break;
            case Cube.TOP:
                nextCell = lastCell.top;
                if (nextCell == null) {
                    return false;
                } else if (nextCell.children == null) {
                    break;
                }
                tmpVector.z += eps.z;
                if (tmpVector.z > max.z) {
                    return false;
                }
                nextCell = getCellFromPoint(nextCell, tmpVector);
                break;
            case Cube.BOTTOM:
                nextCell = lastCell.bottom;
                if (nextCell == null) {
                    return false;
                } else if (nextCell.children == null) {
                    break;
                }
                tmpVector.z -= eps.z;
                if (tmpVector.z < min.z) {
                    return false;
                }
                nextCell = getCellFromPoint(nextCell, tmpVector);
                break;
            case Cube.LEFT:
                nextCell = lastCell.left;
                if (nextCell == null) {
                    return false;
                } else if (nextCell.children == null) {
                    break;
                }
                tmpVector.x -= eps.x;
                if (tmpVector.x < min.x) {
                    return false;
                }
                nextCell = getCellFromPoint(nextCell, tmpVector);
                break;
            case Cube.RIGHT:
                nextCell = lastCell.right;
                if (nextCell == null) {
                    return false;
                } else if (nextCell.children == null) {
                    break;
                }
                tmpVector.x += eps.x;
                if (tmpVector.x > max.x) {
                    return false;
                }
                nextCell = getCellFromPoint(nextCell, tmpVector);
                break;
            default:
                throw new AssertionError();
        }
        enteringParameter = is.parameter;
        enteredFace = is.face ^ 1;
        return true;
    }

    public void setLine(Line line) {
        this.line = line;
        done = false;
        tmpVector.scaleAdd(line.start, line.direction, line.origin);
        if (bounds.contains(tmpVector, true)) {
            nextCell = getCellFromPoint(tree.getRoot(), tmpVector);
            enteringParameter = Double.NEGATIVE_INFINITY;
            enteredFace = -1;
        } else {
            list.clear();
            BoundingBox.computeIntersections(bounds, min, max, line, false, list, -1, -1);
            if (list.size == 0) {
                done = true;
                return;
            }
            nextCell = getCellFromPoint(tree.getRoot(), list.elements[0].getPoint());
            enteringParameter = list.elements[0].parameter;
            enteredFace = list.elements[0].face;
        }
    }

    private Octree.Cell getCellFromPoint(Octree.Cell root, Tuple3d point) {
        tmpVector.sub(point, min);
        int pos = root.position;
        int xs = (pos >> Octree.Cell.X_BIT) & Octree.Cell.POS_MASK;
        int ys = (pos >> Octree.Cell.Y_BIT) & Octree.Cell.POS_MASK;
        int zs = (pos >> Octree.Cell.Z_BIT) & Octree.Cell.POS_MASK;
        int x = (int) (tmpVector.x * coordToGrid.x);
        int y = (int) (tmpVector.y * coordToGrid.y);
        int z = (int) (tmpVector.z * coordToGrid.z);
        int shift = pos & Octree.Cell.WIDTH_MASK;
        int w = (1 << shift) - 1;
        if (x > xs + w) {
            x = xs + w;
        } else if (x < xs) {
            x = xs;
        }
        if (y > ys + w) {
            y = ys + w;
        } else if (y < ys) {
            y = ys;
        }
        if (z > zs + w) {
            z = zs + w;
        } else if (z < zs) {
            z = zs;
        }
        x <<= 2;
        y <<= 1;
        while (root.children != null) {
            shift--;
            int child = ((x >> shift) & 4) + ((y >> shift) & 2) + ((z >> shift) & 1);
            root = root.children[child];
        }
        return root;
    }

    public Octree.Cell next() {
        if (!hasNext()) {
            return null;
        }
        lastCell = nextCell;
        nextCell = null;
        return lastCell;
    }

    public double getEnteringParameter() {
        return enteringParameter;
    }
}
