package org.jazzteam.edu.algo.matrixgame.mathem;

public class WayToSolve {

    public Matrix matr = new Matrix();

    private int[] massivI;

    private int[] massivJ;

    private int minElement;

    private int maxElement;

    private int[] massivIndexX;

    private int[][] massivIndexXY;

    public WayToSolve() {
    }

    public int[] getMassivI() {
        return massivI;
    }

    public int getMassivI(int i) {
        return massivI[i];
    }

    public int getMassivLengthI() {
        return massivI.length;
    }

    public void setMassivI(int[] massivI) {
        this.massivI = massivI;
    }

    public void setMassivI(int i, int element) {
        this.massivI[i] = element;
    }

    public void setMassivI(int x) {
        this.massivI = new int[x];
    }

    public void setElementMassivI(int i, int element) {
        this.massivI[i] = element;
    }

    public int[] getMassivJ() {
        return massivJ;
    }

    public int getMassivLengthJ() {
        return massivJ.length;
    }

    public void setMassivJ(int[] massivJ) {
        this.massivJ = massivJ;
    }

    public void setElementMassivJ(int j, int element) {
        this.massivJ[j] = element;
    }

    public int getMinElement() {
        return minElement;
    }

    public void setMinElement(int minElement) {
        this.minElement = minElement;
    }

    /**
	 * 
	 * @return
	 * @version 1
	 */
    public int getMaxElement() {
        return maxElement;
    }

    /**
	 * 
	 * @param maxElement
	 * @version 1
	 */
    public void setMaxElement(int maxElement) {
        this.maxElement = maxElement;
    }

    /**
	 * 
	 * @return
	 * @version 1
	 */
    public int[] getMassivIndexX() {
        return massivIndexX;
    }

    /**
	 * 
	 * @return
	 * @version 1
	 */
    public int getLengthMassivIndexX() {
        return massivIndexX.length;
    }

    /**
	 * 
	 * @param massivIndexX
	 * @version 1
	 */
    public void setMassivIndexX(int[] massivIndexX) {
        this.massivIndexX = massivIndexX;
    }

    /**
	 * 
	 * @param j
	 * @param element
	 * @version 1
	 */
    public void setElementMassivIndexX(int j, int element) {
        this.massivIndexX[j] = element;
    }

    /**
	 * 
	 * @return
	 * @version 1
	 */
    public int[][] getMassivIndexXY() {
        return massivIndexXY;
    }

    /**
	 * 
	 * @return
	 * @version 1
	 */
    public int getMassivIndexLengthXY() {
        return massivIndexXY.length;
    }

    /**
	 * 
	 * @return
	 * @version 1
	 */
    public int getMassivIndexXLengthY(int i) {
        return massivIndexXY[i].length;
    }

    /**
	 * 
	 * @param massivIndexXY
	 * @version 1
	 */
    public void setMassivIndexXY(int[][] massivIndexXY) {
        this.massivIndexXY = massivIndexXY;
    }

    /**
	 * 
	 * @param i
	 * @param j
	 * @param element
	 * @version 1
	 */
    public void setElementMassivIndexXY(int i, int j, int element) {
        this.massivIndexXY[i][j] = element;
    }

    public void maxElement(int[] massiv) {
        maxElement = massiv[0];
        for (int i = 0; i < massiv.length; i++) {
            if (massiv[i] > maxElement) {
                maxElement = massiv[i];
            }
        }
    }

    public void maxElement(int[][] massiv) {
        maxElement = massiv[0][0];
        for (int i = 1; i >= massiv.length; i++) {
            for (int j = 1; j >= massiv[i].length; j++) {
                if (massiv[i][j] < maxElement) {
                    maxElement = massiv[i][j];
                }
            }
        }
    }

    public void maxElement(int[][] massiv, int num, int way) {
        switch(way) {
            case 0:
                {
                    maxElement = massiv[num][0];
                    for (int j = 1; j < massiv[num].length; j++) {
                        if (massiv[num][j] > maxElement) {
                            maxElement = massiv[num][j];
                        }
                    }
                    break;
                }
            case 1:
                {
                    maxElement = massiv[0][num];
                    for (int i = 1; i < massiv.length; i++) {
                        if (massiv[i][num] > maxElement) {
                            maxElement = massiv[i][num];
                        }
                    }
                    break;
                }
        }
    }

    /**
	 * ���������� ������������ �������� ������ �������
	 */
    public void minElement(int[] massiv) {
        minElement = massiv[0];
        for (int i = 0; i < massiv.length; i++) {
            if (massiv[i] < minElement) {
                minElement = massiv[i];
            }
        }
    }

    /**
	 * ���������� ������������ �������� �������
	 * 
	 * @param massiv
	 *            ��� �����
	 */
    public void minElement(int[][] massiv) {
        minElement = massiv[0][0];
        for (int i = 1; i <= massiv.length; i++) {
            for (int j = 1; j <= massiv[i].length; j++) {
                if (massiv[i][j] < minElement) {
                    minElement = massiv[i][j];
                }
            }
        }
    }

    /**
	 * 
	 * @param massiv
	 * @param num
	 * @param way
	 *            : 0-����������� ������� ������������ ������ ������� 1-����������� ������� ������������ ������� �������
	 */
    public void minElement(int[][] massiv, int num, int way) {
        switch(way) {
            case 0:
                {
                    minElement = massiv[num][0];
                    for (int j = 0; j < massiv[num].length; j++) {
                        if (massiv[num][j] < minElement) {
                            minElement = massiv[num][j];
                        }
                    }
                    break;
                }
            case 1:
                {
                    minElement = massiv[0][num];
                    for (int i = 0; i < massiv.length; i++) {
                        if (massiv[i][num] < minElement) {
                            minElement = massiv[i][num];
                        }
                    }
                    break;
                }
        }
    }

    public int[] addressMeaningfully(int[] massiv, int element) {
        int caunter = 0;
        for (int i = 0; i <= massiv[i]; i++) {
            if (massiv[i] == element) {
                massivIndexX[caunter] = i;
                caunter++;
            }
        }
        return this.massivIndexX;
    }

    /**
	 * ���������� ������ �������� �.�. �������� ����� ������ � ������� ���������
	 * 
	 * @return
	 */
    public void addressMeaningfully(int[][] massiv, int element) {
        int caunter = 0;
        int j = 0;
        massivIndexXY = new int[2][caunter(massiv, element)];
        for (int i = 0; i < massiv.length; i++) {
            for (j = 0; j < massiv[i].length; j++) {
                if (massiv[i][j] == element) {
                    massivIndexXY[0][caunter] = i + 1;
                    massivIndexXY[1][caunter] = j + 1;
                    caunter++;
                }
            }
        }
    }

    public void setMassivIPlus1Item(int element) {
        int[] massiv = new int[getMassivLengthI() + 1];
        for (int i = 0; i < getMassivI().length; i++) {
            massiv[i] = getMassivI(i);
        }
        massiv[massiv.length - 1] = element;
        setMassivI(massiv.length);
        setMassivI(massiv);
    }

    private int caunter(int[][] massiv, int element) {
        int caunt = 0;
        for (int i = 0; i < massiv.length; i++) {
            for (int j = 0; j < massiv[i].length; j++) {
                if (massiv[i][j] == element) {
                    caunt++;
                }
            }
        }
        return caunt;
    }

    public void printMatrix(int element) {
        System.out.println(element);
    }

    public void printMatrix(int[] massiv, int q) {
        switch(q) {
            case 0:
                {
                    for (int i = 0; i < massiv.length; i++) {
                        System.out.println(massiv[i] + " ");
                    }
                    break;
                }
            case 1:
                {
                    for (int i = 0; i < massiv.length; i++) {
                        System.out.print(massiv[i] + " ");
                    }
                    break;
                }
        }
    }

    public void printMatrix(int[][] massiv) {
        for (int i = 0; i < massiv.length; i++) {
            for (int j = 0; j < massiv[i].length; j++) {
                System.out.print(massiv[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
}
