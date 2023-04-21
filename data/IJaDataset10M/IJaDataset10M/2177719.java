package org.gvsig.rastertools.vectorization;

import java.io.File;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.cresques.cts.ProjectionPool;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.raster.vectorization.VectorizationBinding;
import org.gvsig.rastertools.vectorizacion.fmap.BezierPathX;
import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.FPolyline2D;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.drivers.DXFLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.drivers.LayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.SHPLayerDefinition;
import com.iver.cit.gvsig.fmap.edition.DefaultRowEdited;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.fmap.edition.writers.dxf.DxfFieldsMapping;
import com.iver.cit.gvsig.fmap.edition.writers.dxf.DxfWriter;
import com.iver.cit.gvsig.fmap.edition.writers.shp.ShpWriter;

/**
* 
* @version 05/08/2008
* @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
*/
public class TestRasterPotrace {

    private String baseDir = "./test-images/";

    private String fileName = "wheel";

    private String fileIn = baseDir + fileName + ".bmp";

    private String fileOutBase = "/tmp/" + fileName;

    private Value values[];

    private IWriter writer;

    private int m_iGeometry = 0;

    public void generatePotrace(String fileIn, String fileOutBase, int trozos) {
        NumberFormat formatter = new DecimalFormat("000");
        String s = formatter.format(trozos);
        String fileOutShape = fileOutBase + s + ".shp";
        String fieldName = fileName + s;
        try {
            VectorizationBinding binding = new VectorizationBinding(fileIn);
            double geometrias[] = binding.VectorizeBuffer();
            String sFields[] = new String[2];
            sFields[0] = "ID";
            sFields[1] = fieldName + "";
            LayerDefinition tableDef = null;
            if (fileOutShape.endsWith(".dxf")) {
                writer = new DxfWriter();
                ((DxfWriter) writer).setFile(new File(fileOutShape));
                ProjectionPool pool = new ProjectionPool();
                ((DxfWriter) writer).setProjection(pool.get("EPSG:23030"));
                tableDef = new DXFLayerDefinition();
                DxfFieldsMapping fieldsMapping = new DxfFieldsMapping();
                ((DxfWriter) writer).setFieldMapping(fieldsMapping);
            }
            if (fileOutShape.endsWith(".shp")) {
                writer = new ShpWriter();
                ((ShpWriter) writer).setFile(new File(fileOutShape));
                tableDef = new SHPLayerDefinition();
            }
            tableDef.setShapeType(FShape.LINE);
            int types[] = { Types.DOUBLE, Types.DOUBLE };
            FieldDescription[] fields = new FieldDescription[sFields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i] = new FieldDescription();
                fields[i].setFieldName(sFields[i]);
                fields[i].setFieldType(types[i]);
                fields[i].setFieldLength(getDataTypeLength(types[i]));
                fields[i].setFieldDecimalCount(5);
            }
            tableDef.setFieldsDesc(fields);
            tableDef.setName(fileOutShape);
            try {
                writer.initialize(tableDef);
                writer.preProcess();
            } catch (InitializeWriterException e) {
                e.printStackTrace();
            } catch (StartWriterVisitorException e) {
                e.printStackTrace();
            }
            values = new Value[2];
            values[0] = ValueFactory.createValue(0);
            values[1] = ValueFactory.createValue(0);
            showPotrace(geometrias, trozos);
            writer.postProcess();
        } catch (NotSupportedExtensionException e) {
            e.printStackTrace();
        } catch (RasterDriverException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TestRasterPotrace() {
        for (int i = 2; i <= 15; i++) {
            generatePotrace(fileIn, fileOutBase, i);
        }
        generatePotrace(fileIn, fileOutBase, 100);
    }

    public void addShape(FShape shape, Value[] value) throws Exception {
        if (shape == null) return;
        IGeometry geom = ShapeFactory.createGeometry(shape);
        addGeometry(geom, value);
    }

    public void addGeometry(IGeometry geom, Value[] value) throws Exception {
        DefaultFeature feat = new DefaultFeature(geom, value, Integer.toString(m_iGeometry));
        IRowEdited editFeat = new DefaultRowEdited(feat, IRowEdited.STATUS_MODIFIED, m_iGeometry);
        m_iGeometry++;
        writer.process(editFeat);
    }

    private void showPotrace(double[] potraceX, int trozos) throws Exception {
        BezierPathX pathX = new BezierPathX(trozos);
        int cont = 1;
        while (true) {
            if ((cont >= potraceX.length) || (cont >= potraceX[0])) return;
            switch((int) potraceX[cont]) {
                case 0:
                    pathX.moveTo(potraceX[cont + 1], potraceX[cont + 2]);
                    cont += 3;
                    break;
                case 1:
                    pathX.lineTo(potraceX[cont + 1], potraceX[cont + 2]);
                    cont += 3;
                    break;
                case 2:
                    pathX.curveTo(potraceX[cont + 1], potraceX[cont + 2], potraceX[cont + 3], potraceX[cont + 4], potraceX[cont + 5], potraceX[cont + 6]);
                    cont += 7;
                    break;
                case 3:
                    FPolyline2D line = new FPolyline2D(pathX);
                    addShape(line, values);
                    pathX = new BezierPathX(trozos);
                    cont++;
                    break;
            }
        }
    }

    /**
	 * Returns the length of field
	 * @param dataType
	 * @return length of field
	 */
    public int getDataTypeLength(int dataType) {
        switch(dataType) {
            case Types.NUMERIC:
            case Types.DOUBLE:
            case Types.REAL:
            case Types.FLOAT:
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.DECIMAL:
                return 20;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return 254;
            case Types.DATE:
                return 8;
            case Types.BOOLEAN:
            case Types.BIT:
                return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        new TestRasterPotrace();
    }
}
