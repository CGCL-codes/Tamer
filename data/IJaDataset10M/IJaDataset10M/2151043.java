package net.sf.ideoreport.common.config;

import java.awt.Color;
import java.util.Hashtable;
import net.sf.ideoreport.api.datastructure.containers.parameter.IParameterValues;
import net.sf.ideoreport.engines.DefaultEngineGenericTest;

public class DefaultColorCodingConfigTest extends DefaultEngineGenericTest {

    IParameterValues params;

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testColor() {
        IDataContainerConfig dcConfig = this.config.getDataContainer("DC_1");
        DefaultColorGradientConfig vCouleur = (DefaultColorGradientConfig) dcConfig.getColor("Product 1");
        assertEquals("FF", vCouleur.getBlue1());
        assertEquals("FF", vCouleur.getBlue2());
        assertEquals("97", vCouleur.getRed1());
        assertEquals("DB", vCouleur.getRed2());
        assertEquals("D2", vCouleur.getGreen1());
        assertEquals("EF", vCouleur.getGreen2());
        System.out.println("Couleur du produit 1 = " + new Color(Integer.parseInt(vCouleur.getRed1(), 16), Integer.parseInt(vCouleur.getGreen1(), 16), Integer.parseInt(vCouleur.getBlue1(), 16)).toString());
    }

    public void testColorHeritage() {
        IDataContainerConfig dcConfig = this.config.getDataContainer("DC_1_1");
        DefaultColorGradientConfig vCouleur = (DefaultColorGradientConfig) dcConfig.getColor("Product 2");
        assertEquals("AA", vCouleur.getBlue1());
        assertEquals("0", vCouleur.getBlue2());
        assertEquals("FF", vCouleur.getRed1());
        assertEquals("FF", vCouleur.getRed2());
        assertEquals("F4", vCouleur.getGreen1());
        assertEquals("DD", vCouleur.getGreen2());
        System.out.println("Couleur du produit 2 = " + new Color(Integer.parseInt(vCouleur.getRed1(), 16), Integer.parseInt(vCouleur.getGreen1(), 16), Integer.parseInt(vCouleur.getBlue1(), 16)).toString());
    }

    public void testColorPalette() {
        IDataContainerConfig dcConfig = this.config.getDataContainer("DC_2");
        DefaultColorPlainConfig vCouleur = (DefaultColorPlainConfig) dcConfig.getColor("Company 1");
        if (vCouleur.isForceHexa()) {
            Color vCol = new Color(Integer.parseInt(vCouleur.getRed(), 16), Integer.parseInt(vCouleur.getGreen(), 16), Integer.parseInt(vCouleur.getBlue(), 16));
            assertEquals(200, vCol.getRed());
            assertEquals(200, vCol.getGreen());
            assertEquals(200, vCol.getBlue());
            System.out.println("Couleur de la company 1 = " + vCol.toString());
        }
    }

    public void testColorConfig() {
        IDataContainerConfig dcConfig = this.config.getDataContainer("DC_1");
        DefaultColorGradientConfig vCouleur = (DefaultColorGradientConfig) dcConfig.getColor("Product 3");
        if (vCouleur.isForceHexa()) {
            Color vCol = new Color(Integer.parseInt(vCouleur.getRed1(), 16), Integer.parseInt(vCouleur.getGreen1(), 16), Integer.parseInt(vCouleur.getBlue1(), 16));
            assertEquals(174, vCol.getRed());
            assertEquals(96, vCol.getGreen());
            assertEquals(194, vCol.getBlue());
            System.out.println("Couleur du produit 3 = " + vCol.toString());
        }
    }

    public void testColorKO() {
        IDataContainerConfig dcConfig = this.config.getDataContainer("DC_1");
        DefaultColorPlainConfig vCouleur = (DefaultColorPlainConfig) dcConfig.getColor("Company 1");
        try {
            new Color(Integer.parseInt(vCouleur.getRed(), 16), Integer.parseInt(vCouleur.getGreen(), 16), Integer.parseInt(vCouleur.getBlue(), 16));
        } catch (Exception e) {
            System.err.println("La couleur ne respecte pas l'hexadecimal " + e);
            e.printStackTrace();
        }
        DefaultColorPlainConfig vCouleur2 = (DefaultColorPlainConfig) dcConfig.getColor("Company 2");
        try {
            new Color(Integer.parseInt(vCouleur2.getRed(), 16), Integer.parseInt(vCouleur2.getGreen(), 16), Integer.parseInt(vCouleur2.getBlue(), 16));
        } catch (Exception e) {
            System.err.println("Il manque une composante � la couleur " + e);
        }
        IDataContainerConfig dcConfig2 = this.config.getDataContainer("DC_1_1");
        DefaultColorPlainConfig vCoul2 = (DefaultColorPlainConfig) dcConfig2.getColor("Company 1");
        Color test = null;
        try {
            test = new Color(Integer.parseInt(vCoul2.getRed(), 16), Integer.parseInt(vCoul2.getGreen(), 16), Integer.parseInt(vCoul2.getBlue(), 16));
        } catch (Exception e) {
            System.err.println("La couleur ne respecte pas l'hexadecimal " + e);
        }
        System.out.println("couleur = " + test.toString());
    }

    public void testGetColorCodingConfigs() {
        IDataContainerConfig dcConfig = this.config.getDataContainer("DC_1");
        Hashtable vColorCodingConfigs = dcConfig.getColorCodingConfigs();
        IColorCodingConfig test = (IColorCodingConfig) vColorCodingConfigs.get("Companies");
        assertNotNull(test);
    }
}
