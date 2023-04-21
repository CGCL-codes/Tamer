package net.ivoa.fits.test;

import java.io.File;
import java.io.FileOutputStream;
import net.ivoa.fits.Fits;
import net.ivoa.fits.FitsFactory;
import net.ivoa.fits.data.BinaryTable;
import net.ivoa.fits.hdu.BasicHDU;
import net.ivoa.fits.hdu.BinaryTableHDU;
import net.ivoa.util.BufferedDataOutputStream;
import net.ivoa.util.BufferedFile;
import junit.framework.TestCase;

/**
 * 
 * 
 * @author Petr Kubanek <Petr.Kubanek@obs.unige.ch>
 */
public class TestBinaryTable extends TestCase {

    private BinaryTableHDU bt;

    private Fits file;

    private byte[] bytes;

    private byte[][] bits;

    private boolean[] bools;

    private short[][] shorts;

    private int[] ints;

    private float[][][] floats;

    private double[] doubles;

    private long[] longs;

    private String[] strings;

    float[][] vf;

    short[][] vs;

    double[][] vd;

    boolean[][] vbool;

    public TestBinaryTable(String name) {
        super(name);
        bytes = new byte[50];
        bits = new byte[50][2];
        bools = new boolean[50];
        shorts = new short[50][3];
        ints = new int[50];
        floats = new float[50][4][4];
        doubles = new double[50];
        longs = new long[50];
        strings = new String[50];
        for (int i = 0; i < bytes.length; i += 1) {
            bytes[i] = (byte) (2 * i);
            bits[i][0] = bytes[i];
            bits[i][1] = (byte) (~bytes[i]);
            bools[i] = (bytes[i] % 8) == 0 ? true : false;
            shorts[i][0] = (short) (2 * i);
            shorts[i][1] = (short) (3 * i);
            shorts[i][2] = (short) (4 * i);
            ints[i] = i * i;
            for (int j = 0; j < 4; j += 1) {
                for (int k = 0; k < 4; k += 1) {
                    floats[i][j][k] = (float) (i + j * Math.exp(k));
                }
            }
            doubles[i] = 3 * Math.sin(i);
            longs[i] = i * i * i * i;
            strings[i] = "abcdefghijklmnopqrstuvwxzy".substring(0, i % 20);
        }
        vf = new float[50][];
        vs = new short[50][];
        vd = new double[50][];
        vbool = new boolean[50][];
        for (int i = 0; i < 50; i += 1) {
            vf[i] = new float[i + 1];
            vf[i][i / 2] = i * 3;
            vs[i] = new short[i / 10 + 1];
            vs[i][i / 10] = (short) -i;
            vd[i] = new double[i % 2 == 0 ? 1 : 2];
            vd[i][0] = 99.99;
            vbool[i] = new boolean[i / 10];
            if (i >= 10) {
                vbool[i][0] = i % 2 == 1;
            }
        }
        FitsFactory.setUseAsciiTables(false);
    }

    public void testFile1() throws Exception {
        file = new Fits("data/testbinarytable1.fits");
        bt = (BinaryTableHDU) file.getHDU(1);
    }

    public void testFile2() throws Exception {
        file = new Fits("data/testbinarytable2.fits");
        bt = (BinaryTableHDU) file.getHDU(1);
    }

    /**
	 * This method tests the binary table classes for the Java FITS library,
	 * notably BinaryTableHDU, BinaryTable, FitsHeap and the utility class
	 * ColumnTable. Tests include:
	 * 
	 * <pre>
	 * 
	 *  
	 *   
	 *    
	 *     
	 *                                Reading and writing data of all valid types.
	 *                                Reading and writing variable length da
	 *                                Creating binary tables from:
	 *                                   Object[][] array
	 *                                   Object[] array
	 *                                   ColumnTable
	 *                                   Column x Column
	 *                                   Row x Row
	 *                                Read binary table
	 *                                   Row x row
	 *                                   Element x element
	 *                                Modify
	 *                                   Row, column, element
	 *                                Rewrite binary table in place
	 *     
	 *    
	 *   
	 *  
	 * </pre>
	 */
    public void testTable1() throws Exception {
        Fits f = new Fits();
        f.addHDU(Fits.makeHDU(new Object[] { bytes, bits, bools, shorts, ints, floats, doubles, longs, strings }));
        BinaryTableHDU bhdu = (BinaryTableHDU) f.getHDU(1);
        bhdu.setColumnName(0, "bytes", null);
        bhdu.setColumnName(1, "bits", "bits later on");
        bhdu.setColumnName(6, "doubles", null);
        bhdu.setColumnName(5, "floats", "4 x 4 array");
        BufferedFile bf = new BufferedFile("bt1.fits", "rw");
        f.write(bf);
        bf.flush();
        bf.close();
        f = new Fits("bt1.fits");
        BasicHDU hdu;
        f.read();
        for (int i = 0; i < f.getNumberOfHDUs(); i += 1) {
            hdu = f.getHDU(i);
        }
        BinaryTableHDU thdu = (BinaryTableHDU) f.getHDU(1);
        byte[] tb = (byte[]) thdu.getColumn(0);
        byte[][] tbits = (byte[][]) thdu.getColumn(1);
        boolean[] tbools = (boolean[]) thdu.getColumn(2);
        short[][] tsh = (short[][]) thdu.getColumn(3);
        int[] tints = (int[]) thdu.getColumn(4);
        float[][][] tflt = (float[][][]) thdu.getColumn(5);
        double[] tdoub = (double[]) thdu.getColumn(6);
        long[] tlong = (long[]) thdu.getColumn(7);
        String[] tstr = (String[]) thdu.getColumn(8);
        for (int i = 0; i < tb.length; i += 1) {
            assertEquals(bytes[i], tb[i]);
            for (int j = 0; j < 2; j += 1) {
                assertEquals(bits[i][j], tbits[i][j]);
            }
            for (int j = 0; j < 3; j += 1) {
                assertEquals(shorts[i][j], tsh[i][j]);
            }
            for (int j = 0; j < 4; j += 1) {
                for (int k = 0; k < 4; k += 1) {
                    assertEquals(floats[i][j][k], tflt[i][j][k], 0.0f);
                }
            }
            assertEquals(bools[i], tbools[i]);
            assertEquals(ints[i], tints[i]);
            assertEquals(doubles[i], tdoub[i], 0);
            assertEquals(longs[i], tlong[i]);
            assertEquals(strings[i], tstr[i].trim());
        }
    }

    public void testTable2() throws Exception {
        BasicHDU hdu = Fits.makeHDU(new Object[] { floats, vf, vs, vd, shorts, vbool });
        Fits f = new Fits();
        f.addHDU(hdu);
        BufferedDataOutputStream bdos = new BufferedDataOutputStream(new FileOutputStream("bt2.fits"));
        f.write(bdos);
        f = new Fits("bt2.fits");
        f.read();
        for (int i = 0; i < f.getNumberOfHDUs(); i += 1) {
            f.getHDU(i).toString();
        }
        BinaryTableHDU bhdu = (BinaryTableHDU) f.getHDU(1);
        float[][] tvf = (float[][]) bhdu.getColumn(1);
        short[][] tvs = (short[][]) bhdu.getColumn(2);
        double[][] tvd = (double[][]) bhdu.getColumn(3);
        boolean[][] tvbool = (boolean[][]) bhdu.getColumn(5);
        for (int i = 0; i < 50; i += 10) {
            assertEquals(vf[i].length, tvf[i].length);
            assertEquals(vs[i].length, tvs[i].length);
            assertEquals(vd[i].length, tvd[i].length);
            assertEquals(vbool[i].length, tvbool[i].length);
            for (int j = 0; j < tvf[i].length; j += 1) {
                assertEquals(vf[i][j], tvf[i][j], 0);
            }
            for (int j = 0; j < tvs[i].length; j += 1) {
                assertEquals(vs[i][j], tvs[i][j]);
            }
            for (int j = 0; j < tvd[i].length; j += 1) {
                assertEquals(vd[i][j], tvd[i][j], 0);
            }
            for (int j = 0; j < tvbool[i].length; j += 1) {
                assertEquals(vbool[i][j], tvbool[i][j]);
            }
        }
    }

    public void testTable3() throws Exception {
        BinaryTable btab = new BinaryTable();
        btab.addColumn(floats);
        btab.addColumn(vf);
        btab.addColumn(strings);
        btab.addColumn(vbool);
        btab.addColumn(ints);
        Fits f = new Fits();
        f.addHDU(Fits.makeHDU(btab));
        BufferedDataOutputStream bdos = new BufferedDataOutputStream(new FileOutputStream("bt3.fits"));
        f.write(bdos);
        f = new Fits("bt3.fits");
        BinaryTableHDU bhdu = (BinaryTableHDU) f.getHDU(1);
        btab = (BinaryTable) bhdu.getData();
        float[] flatfloat = (float[]) btab.getFlattenedColumn(0);
        float[][] tvf = (float[][]) btab.getColumn(1);
        String[] xstr = (String[]) btab.getColumn(2);
        boolean[][] tvbool = (boolean[][]) btab.getColumn(3);
        for (int i = 0; i < 50; i += 3) {
            assertEquals(flatfloat[16 * i], flatfloat[16 * i + 1], 0);
            assertEquals(vf[i].length, tvf[i].length);
            for (int j = 0; j < vf[i].length; j++) {
                assertEquals(vf[i][j], tvf[i][j], 0);
            }
            assertEquals(strings[i], xstr[i]);
            assertEquals(vbool[i].length, tvbool[i].length);
            for (int j = 0; j < vbool[i].length; j++) {
                assertEquals(vbool[i][j], tvbool[i][j]);
            }
        }
        btab.addColumn(floats);
        btab.addColumn(vf);
        btab.addColumn(strings);
        btab.addColumn(vbool);
        btab.addColumn(ints);
        for (int i = 0; i < 50; i += 1) {
            Object[] row = btab.getRow(i);
            assertEquals(10, row.length);
            float[] qx = (float[]) row[1];
            assertEquals(strings[i], (String) row[2]);
            row[2] = "new string:" + i;
            btab.addRow(row);
        }
        f = new Fits();
        f.addHDU(Fits.makeHDU(btab));
        BufferedFile bf = new BufferedFile("bt4.fits", "rw");
        f.write(bf);
        bf.flush();
        bf.close();
        f = new Fits("bt4.fits");
        btab = (BinaryTable) f.getHDU(1).getData();
        xstr = (String[]) btab.getColumn(2);
        for (int i = 0; i < xstr.length; i++) {
            boolean[] ba = (boolean[]) btab.getElement(i, 3);
            float[] fx = (float[]) btab.getElement(i, 1);
            float[][] tst = (float[][]) btab.getElement(i, 0);
            String s = (String) btab.getElement(i, 2);
            int trow = i % 50;
            assertEquals(vbool[trow].length, ba.length);
            for (int j = 0; j < ba.length; j += 1) {
                assertEquals(ba[j], vbool[trow][j]);
            }
            assertEquals(vf[trow].length, fx.length);
            for (int j = 0; j < fx.length; j += 1) {
                assertEquals(vf[trow][j], fx[j], 0);
            }
            if (i >= 50) assertEquals("new string:" + trow, s); else assertEquals(strings[i], s);
            ba = (boolean[]) btab.getElement(i, 8);
            fx = (float[]) btab.getElement(i, 6);
            tst = (float[][]) btab.getElement(i, 5);
            s = (String) btab.getElement(i, 7);
            assertEquals(vbool[trow].length, ba.length);
            for (int j = 0; j < ba.length; j += 1) {
                assertEquals(ba[j], vbool[trow][j]);
            }
            assertEquals(vf[trow].length, fx.length);
            for (int j = 0; j < fx.length; j += 1) {
                assertEquals(vf[trow][j], fx[j], 0);
            }
            assertEquals(strings[trow], s);
        }
        f.getHDU(1).getData();
        xstr = (String[]) btab.getColumn(2);
        for (int i = 0; i < xstr.length; i += 3) {
            int trow = i % 50;
            String s = (String) btab.getElement(i, 2);
            if (i > 50) assertEquals("new string:" + trow, s); else assertEquals(strings[i], s);
            assertEquals(s, xstr[i]);
        }
        Object[][] x = new Object[5][3];
        for (int i = 0; i < 5; i += 1) {
            x[i][0] = new float[] { i };
            x[i][1] = new String("AString" + i);
            x[i][2] = new int[][] { { i, 2 * i }, { 3 * i, 4 * i } };
        }
        f = new Fits();
        FitsFactory.setUseAsciiTables(false);
        BasicHDU hdu = Fits.makeHDU(x);
        hdu.toString();
        f.addHDU(hdu);
        bf = new BufferedFile("bt5.fits", "rw");
        f.write(bf);
        bf.close();
        File fi = new File("bt1.fits");
        fi.delete();
        fi = new File("bt2.fits");
        fi.delete();
        fi = new File("bt3.fits");
        fi.delete();
        fi = new File("bt4.fits");
        fi.delete();
        fi = new File("bt5.fits");
        fi.delete();
    }
}
