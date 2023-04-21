package gothwag.mapgeneration;

import gothwag.mapgeneration.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author chenyu
 * @version 1.0
 */
public class SoilLayer {

    private SoilDetail[][] m_soil_array;

    private int m_x_size;

    private int m_y_size;

    public SoilLayer() {
    }

    public boolean isEmpty() {
        return m_x_size == 0;
    }

    public void importLayer(Layer i_layer, String i_name, String i_description) {
        int[][] t_space = i_layer.output();
        m_y_size = i_layer.getYSize();
        m_x_size = i_layer.getXSize();
        m_soil_array = new SoilDetail[m_y_size][m_x_size];
        for (int i = 0; i < m_y_size; i++) {
            for (int j = 0; j < m_x_size; j++) {
                if (t_space[i][j] == Line.HAVE_OBJECT) {
                    m_soil_array[i][j] = new SoilDetail();
                    m_soil_array[i][j].name = i_name;
                    m_soil_array[i][j].description = i_description;
                } else {
                    m_soil_array[i][j] = null;
                }
            }
        }
    }

    /**
   * <pre>
   * algorithm:
       * 1. the even column's position doesn't change, and the odd column's position
   *    should be moved upper by half position
   *
   * </pre>
   * @return
   */
    public Soil[][] outputSoil() {
        Soil[][] t_soil = new Soil[m_y_size][m_x_size];
        Soil[] t_neighbours = null;
        int t_x = 0;
        int t_y = 0;
        for (int i = 0; i < m_y_size; i++) {
            for (int j = 0; j < m_x_size; j++) {
                t_soil[i][j] = new Soil(m_soil_array[i][j].name, m_soil_array[i][j].description);
            }
        }
        for (int i = 0; i < m_y_size; i++) {
            for (int j = 0; j < m_x_size; j++) {
                t_neighbours = new Soil[6];
                t_y = i - 1;
                t_x = j;
                if (t_y >= 0) {
                    t_neighbours[Soil.UPPER] = t_soil[t_y][t_x];
                } else {
                    t_neighbours[Soil.UPPER] = null;
                }
                t_y = i + 1;
                t_x = j;
                if (t_y < m_y_size) {
                    t_neighbours[Soil.LOWER] = t_soil[t_y][t_x];
                } else {
                    t_neighbours[Soil.LOWER] = null;
                }
                if (j % 2 == 0) {
                    t_y = i;
                    t_x = j + 1;
                    if (t_x < m_x_size) {
                        t_neighbours[Soil.NORTHEAST] = t_soil[t_y][t_x];
                    } else {
                        t_neighbours[Soil.NORTHEAST] = null;
                    }
                } else {
                    t_y = i - 1;
                    t_x = j + 1;
                    if (t_y >= 0 && (t_x < m_x_size)) {
                        t_neighbours[Soil.NORTHEAST] = t_soil[t_y][t_x];
                    } else {
                        t_neighbours[Soil.NORTHEAST] = null;
                    }
                }
                if (j % 2 == 0) {
                    t_y = i;
                    t_x = j - 1;
                    if (t_x >= 0) {
                        t_neighbours[Soil.NORTHWEST] = t_soil[t_y][t_x];
                    } else {
                        t_neighbours[Soil.NORTHWEST] = null;
                    }
                } else {
                    t_y = i - 1;
                    t_x = j - 1;
                    if (t_y >= 0 && (t_x >= 0)) {
                        t_neighbours[Soil.NORTHWEST] = t_soil[t_y][t_x];
                    } else {
                        t_neighbours[Soil.NORTHWEST] = null;
                    }
                }
                if (j % 2 == 0) {
                    t_y = i + 1;
                    t_x = j + 1;
                    if (t_y < m_y_size && (t_x < m_x_size)) {
                        t_neighbours[Soil.SOUTHEAST] = t_soil[t_y][t_x];
                    } else {
                        t_neighbours[Soil.SOUTHEAST] = null;
                    }
                } else {
                    t_y = i;
                    t_x = j + 1;
                    if (t_x < m_x_size) {
                        t_neighbours[Soil.SOUTHEAST] = t_soil[t_y][t_x];
                    } else {
                        t_neighbours[Soil.SOUTHEAST] = null;
                    }
                }
                if (j % 2 == 0) {
                    t_y = i + 1;
                    t_x = j - 1;
                    if (t_y < m_y_size && (t_x >= 0)) {
                        t_neighbours[Soil.SOUTHWEST] = t_soil[t_y][t_x];
                    } else {
                        t_neighbours[Soil.SOUTHWEST] = null;
                    }
                } else {
                    t_y = i;
                    t_x = j - 1;
                    if (t_x >= 0) {
                        t_neighbours[Soil.SOUTHWEST] = t_soil[t_y][t_x];
                    } else {
                        t_neighbours[Soil.SOUTHWEST] = null;
                    }
                }
                t_soil[i][j].setNeighbours(t_neighbours);
            }
        }
        return t_soil;
    }

    public void cover(SoilLayer i_upper_layer) {
        for (int i = 0; i < m_y_size; i++) {
            for (int j = 0; j < m_x_size; j++) {
                if (i_upper_layer.m_soil_array[i][j] != null) {
                    m_soil_array[i][j].name = i_upper_layer.m_soil_array[i][j].name;
                    m_soil_array[i][j].description = i_upper_layer.m_soil_array[i][j].description;
                }
            }
        }
    }

    public void testCoverAuxA() {
        m_x_size = 3;
        m_y_size = 3;
        m_soil_array = new SoilDetail[m_y_size][m_x_size];
        for (int i = 0; i < m_y_size; i++) {
            for (int j = 0; j < m_x_size; j++) {
                m_soil_array[i][j] = new SoilDetail();
                m_soil_array[i][j].name = "name A & " + String.valueOf(i) + "& " + String.valueOf(j);
                m_soil_array[i][j].description = "description A & " + String.valueOf(i) + "& " + String.valueOf(j);
            }
        }
    }

    public void testCoverAuxB() {
        m_x_size = 3;
        m_y_size = 3;
        m_soil_array = new SoilDetail[m_y_size][m_x_size];
        for (int i = 0; i < m_y_size; i++) {
            for (int j = 0; j < m_x_size; j++) {
                if (i == j) {
                    m_soil_array[i][j] = new SoilDetail();
                    m_soil_array[i][j].name = "name B & " + String.valueOf(i) + "& " + String.valueOf(j);
                    m_soil_array[i][j].description = "description B & " + String.valueOf(i) + "& " + String.valueOf(j);
                }
            }
        }
    }

    public static void testCover() {
        SoilLayer layerA = new SoilLayer();
        layerA.testCoverAuxA();
        SoilLayer layerB = new SoilLayer();
        layerB.testCoverAuxB();
        layerA.cover(layerB);
        Soil.displaySoil(layerA.outputSoil());
    }

    public void testOutputSoil() {
        m_x_size = 3;
        m_y_size = 3;
        m_soil_array = new SoilDetail[m_y_size][m_x_size];
        for (int i = 0; i < m_y_size; i++) {
            for (int j = 0; j < m_x_size; j++) {
                m_soil_array[i][j] = new SoilDetail();
                if (m_soil_array[i][j] == null) {
                    System.out.println("here");
                }
                m_soil_array[i][j].name = "name& " + String.valueOf(i) + "& " + String.valueOf(j);
                m_soil_array[i][j].description = "description& " + String.valueOf(i) + "& " + String.valueOf(j);
            }
        }
        Soil.displaySoil(this.outputSoil());
    }

    public static void main(String[] args) {
        SoilLayer.testCover();
    }

    class SoilDetail {

        public String name;

        public String description;
    }
}
