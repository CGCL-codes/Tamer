package org.jspar.place;

import java.util.ArrayList;
import java.util.List;
import org.jspar.tile.ITileContent;
import org.jspar.tile.Position;
import org.jspar.tile.Tile;

public class TileOperations {

    private static final int FURTHER = 0;

    private static final int CLOSER = 1;

    public static Tile angledTileInsertion(Tile currentTile, Position anchor, SideSelector selector, Position pivot, int width, int height, ITileContent box) {
        if (selector.hasNoSide()) return null;
        Tile new_ = null;
        int tx = 0;
        int ty = 0;
        List<Tile> lineList = findAllTilesAlongLine(currentTile, anchor, selector, new ArrayList<Tile>());
        Tile cornerTile = firstNoncontiguousFullTile(lineList);
        int newAnchX = anchor.x();
        int newAnchY = anchor.y();
        while (lineList.size() > 0) {
            new_ = angledTileHelp(currentTile, newAnchX, newAnchY, selector, pivot, width, height, box, cornerTile);
            if (new_ != null) break;
            if (selector.xSideCount != 0) {
                ty = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.x() - newAnchX)) + newAnchY;
            }
            if (selector.ySideCount != 0) {
                tx = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.x() - newAnchY)) + newAnchX;
            }
            if (ty >= cornerTile.y() && ty <= cornerTile.bottom()) newAnchY = ty; else newAnchY = cornerTile.y();
            if (tx >= cornerTile.x() && tx <= cornerTile.right()) newAnchX = tx; else newAnchY = cornerTile.x();
            cornerTile = firstNoncontiguousFullTile(lineList);
        }
        if (new_ == null) new_ = angledTileHelp(currentTile, newAnchX, newAnchY, selector, pivot, width, height, box, cornerTile);
        if (new_ == null) throw new RuntimeException("angled_tile_insertion: Complete Failure");
        return new_;
    }

    private static List<Tile> findAllTilesAlongLine(Tile currentTile, Position anchor, SideSelector selector, List<Tile> soFar) {
        Tile neighbor;
        int delX = selector.xSideCount;
        int delY = selector.ySideCount;
        int x, y, xx = 0, yy = 0;
        int bx, by;
        boolean yset = false, xset = false;
        int nextX = 0, nextY = 0;
        Tile temp = currentTile.locate(anchor);
        if (temp == null) return soFar;
        if (temp == currentTile) {
            if (temp.x() == anchor.x() || temp.right() == anchor.x()) {
                return soFar;
            }
            if (temp.y() == anchor.y() || temp.bottom() == anchor.y()) {
                return soFar;
            }
        }
        if (!soFar.contains(temp)) soFar.add(temp);
        if (delX != 0) {
            xx = (delX < 0) ? temp.x() : temp.right();
            by = (anchor.y() - (int) ((float) delY / (float) delX * (float) anchor.x()));
            y = (int) ((float) delY / (float) delX * (float) xx) + by;
            nextY = (int) ((float) delY / (float) delX * ((float) xx + (float) delX / Math.abs(delX))) + by;
            if (y <= temp.bottom() && y >= temp.y()) yset = true;
        }
        if (delY != 0) {
            yy = (delY < 0) ? temp.y() : temp.bottom();
            bx = (anchor.x() - (int) ((float) delX / (float) delY * (float) anchor.y()));
            x = (int) ((float) delX / (float) delY * (float) yy) + bx;
            nextX = (int) ((float) delX / (float) delY * ((float) yy + (float) delY / Math.abs(delY))) + bx;
            if (x <= temp.right() && x >= temp.x()) xset = true;
        }
        if (yset && xset) {
            y = yy;
            x = xx;
            if (delY > 0) {
                if (delX > 0) {
                    if (temp.topRight() != null) soFar.add(temp.topRight());
                    if (temp.rightTop() != null) soFar.add(temp.rightTop());
                } else {
                    neighbor = temp.locate(new Position(x - 1, y + 1));
                    if (neighbor != null) soFar.add(neighbor);
                    neighbor = temp.locate(new Position(x - 1, y - 1));
                    if (neighbor != null) soFar.add(neighbor);
                }
            } else {
                if (delX > 0) {
                    neighbor = temp.locate(new Position(x + 1, y - 1));
                    if (neighbor != null) soFar.add(neighbor);
                    neighbor = temp.locate(new Position(x - 1, y - 1));
                    if (neighbor != null) soFar.add(neighbor);
                } else {
                    if (temp.leftBottom() != null) soFar.add(temp.leftBottom());
                    if (temp.bottomLeft() != null) soFar.add(temp.bottomLeft());
                }
            }
        } else if (yset == true) {
            if (delX < 0) nextX = x = temp.x(); else nextX = x = temp.x() + temp.width() + 1;
        } else if (xset == true) {
            if (delY < 0) nextY = y = temp.y(); else nextY = y = temp.y() + temp.height() + 1;
        } else {
            throw new RuntimeException("find_all_tiles_along_line: can't find anything!");
        }
        return findAllTilesAlongLine(temp, new Position(nextX, nextY), selector, soFar);
    }

    private static Tile firstNoncontiguousFullTile(List<Tile> tileList) {
        boolean returnNextFullTile = false;
        while (tileList.size() > 0) {
            Tile tl = tileList.remove(0);
            if (returnNextFullTile == true && tl.isSolid()) {
                return tl;
            } else if (!tl.isSolid() && tileList.size() > 0) {
                returnNextFullTile = true;
            }
        }
        return null;
    }

    private static Tile angledTileHelp(Tile currentTile, int anchX, int anchY, SideSelector selector, Position pivot, int sizeX, int sizeY, ITileContent box, Tile cornerTile) {
        int epsilon, b, currentX, maxX, minX;
        int orgX = 0, orgY = 0, tempOrgX, tempOrgY;
        int x, y;
        boolean foundOne = false;
        if (selector.xSideCount != 0 && Math.abs(selector.xSideCount) > Math.abs(selector.ySideCount)) {
            if (cornerTile != null) {
                if (selector.xSideCount > 0) {
                    minX = anchX;
                    y = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.x() - anchX)) + anchY;
                    if (selector.ySideCount == 0) maxX = cornerTile.x(); else if (selector.ySideCount > 0) {
                        if (y < cornerTile.y()) {
                            maxX = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.y() - anchY)) + anchX;
                        } else {
                            maxX = cornerTile.x();
                        }
                    } else {
                        if (y > cornerTile.bottom()) {
                            maxX = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.bottom() - anchY)) + anchX;
                        } else {
                            maxX = cornerTile.x();
                        }
                    }
                } else {
                    minX = anchX;
                    y = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.right() - anchX)) + anchY;
                    if (selector.ySideCount == 0) maxX = cornerTile.x() + cornerTile.width(); else if (selector.ySideCount > 0) {
                        if (y < cornerTile.bottom()) {
                            maxX = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.y() - anchY)) + anchX;
                        } else {
                            maxX = cornerTile.x() + cornerTile.width();
                        }
                    } else {
                        if (y > cornerTile.bottom()) {
                            maxX = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.bottom() - anchY)) + anchX;
                        } else {
                            maxX = cornerTile.x() + cornerTile.width();
                        }
                    }
                }
            } else {
                if (selector.xSideCount > 0) {
                    cornerTile = currentTile.find_LRHC();
                    minX = anchX;
                    maxX = cornerTile.right();
                } else {
                    cornerTile = currentTile.find_ULHC();
                    minX = anchX;
                    maxX = cornerTile.x();
                }
            }
            epsilon = currentX = (minX + maxX) / 2;
            b = (anchY - (int) ((float) selector.ySideCount / (float) selector.xSideCount * (float) anchX));
            tempOrgX = currentX - pivot.x();
            tempOrgY = (int) (((float) selector.ySideCount / (float) selector.xSideCount) * (float) currentX) + b - pivot.y();
            if (currentTile.locate(tempOrgX, tempOrgY) == null) {
                epsilon = currentX = currentX / 2;
                System.err.println("Bad origin point selected; trying again...");
            }
            while (Math.abs(epsilon) > 1) {
                tempOrgX = currentX - pivot.x();
                tempOrgY = (int) (((float) selector.ySideCount / (float) selector.xSideCount) * (float) currentX) + b - pivot.y();
                Tile flag = currentTile.areaSearch(tempOrgX, tempOrgY, sizeX, sizeY);
                if (flag != null) {
                    NextMoveResult res = nextMove(minX, maxX, currentX, FURTHER, epsilon);
                    currentX = res.result;
                    minX = res.min;
                    maxX = res.max;
                    epsilon = res.epsilon;
                } else {
                    NextMoveResult res = nextMove(minX, maxX, currentX, CLOSER, epsilon);
                    currentX = res.result;
                    minX = res.min;
                    maxX = res.max;
                    epsilon = res.epsilon;
                    orgX = tempOrgX;
                    orgY = tempOrgY;
                    foundOne = true;
                }
            }
        } else if (selector.ySideCount != 0) {
            if (cornerTile != null) {
                if (selector.ySideCount > 0) {
                    minX = anchY;
                    x = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.y() - anchY)) + anchX;
                    if (selector.xSideCount == 0) maxX = cornerTile.y(); else if (selector.xSideCount > 0) {
                        if (x > cornerTile.x()) {
                            maxX = cornerTile.y();
                        } else {
                            maxX = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.x() - anchX)) + anchY;
                        }
                    } else {
                        if (x > cornerTile.right()) {
                            maxX = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.right() - anchX)) + anchY;
                        } else {
                            maxX = cornerTile.y();
                        }
                    }
                } else {
                    minX = anchY;
                    x = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.bottom() - anchY)) + anchX;
                    if (selector.xSideCount == 0) maxX = cornerTile.bottom(); else if (selector.xSideCount > 0) {
                        if (x > cornerTile.x()) {
                            maxX = cornerTile.bottom();
                        } else {
                            maxX = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.x() - anchX)) + anchY;
                        }
                    } else {
                        if (x > cornerTile.bottom()) {
                            maxX = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.right() - anchX)) + anchY;
                        } else {
                            maxX = cornerTile.bottom();
                        }
                    }
                }
            } else {
                if (selector.ySideCount > 0) {
                    cornerTile = currentTile.find_ULHC();
                    minX = anchY;
                    maxX = cornerTile.bottom();
                } else {
                    cornerTile = currentTile.find_LRHC();
                    maxX = cornerTile.y();
                    minX = anchY;
                }
            }
            epsilon = currentX = (minX + maxX) / 2;
            b = (anchX - (int) (((float) selector.xSideCount / (float) selector.ySideCount) * (float) anchY));
            tempOrgY = currentX - pivot.y();
            tempOrgX = (int) (((float) selector.xSideCount / (float) selector.ySideCount) * (float) currentX) + b - pivot.x();
            if (currentTile.locate(tempOrgX, tempOrgY) == null) {
                currentTile.locate(tempOrgX, tempOrgY);
                epsilon = currentX = currentX / 2;
                System.err.println("Bad origin point selected; trying again...\n");
            }
            while (Math.abs(epsilon) > 1) {
                tempOrgY = currentX - pivot.y();
                tempOrgX = (int) (((float) selector.xSideCount / (float) selector.ySideCount) * (float) currentX) + b - pivot.x();
                Tile flag = currentTile.areaSearch(tempOrgX, tempOrgY, sizeX, sizeY);
                if (flag != null) {
                    NextMoveResult res = nextMove(minX, maxX, currentX, FURTHER, epsilon);
                    currentX = res.result;
                    minX = res.min;
                    maxX = res.max;
                    epsilon = res.epsilon;
                } else {
                    NextMoveResult res = nextMove(minX, maxX, currentX, CLOSER, epsilon);
                    currentX = res.result;
                    minX = res.min;
                    maxX = res.max;
                    epsilon = res.epsilon;
                    orgX = tempOrgX;
                    orgY = tempOrgY;
                    foundOne = true;
                }
            }
        } else {
            System.err.println("angled_tile_help: null <selector.ySideCount>, <selector.xSideCount> values given.\n");
        }
        if (!foundOne) return null;
        return currentTile.insertTile(orgX, orgY, sizeX, sizeY, box);
    }

    private static class NextMoveResult {

        int epsilon;

        int max;

        int min;

        int result;

        public NextMoveResult(int min, int max, int epsilon) {
            this.epsilon = epsilon;
            this.max = max;
            this.min = min;
        }
    }

    private static NextMoveResult nextMove(int min, int max, int current, int direction, int epsilon) {
        NextMoveResult result = new NextMoveResult(min, max, epsilon);
        if (direction == FURTHER) {
            result.epsilon = (epsilon == 1) ? 0 : (max - current + 1) / 2;
            result.min = current;
            result.result = min + epsilon;
        } else {
            result.epsilon = (epsilon == 1) ? 0 : (current - min + 1) / 2;
            result.max = current;
            result.result = max - epsilon;
        }
        return result;
    }
}
