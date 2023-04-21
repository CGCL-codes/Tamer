package com.iver.cit.gvsig.fmap.drivers.sde;

import java.awt.geom.Rectangle2D;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.drivers.IVectorialDatabaseDriver;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.utiles.XMLEntity;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public interface IVectorialSDEDriver extends IVectorialDatabaseDriver {

    public IFeatureIterator getFeatureIterator(String sql);

    public void open();

    public int getDefaultPort();

    public void setWorkingArea(Rectangle2D rect);

    public Rectangle2D getWorkingArea();

    public String getConnectionString(String _host, String _port, String _db, String _user, String _pw);

    public String getTableName();

    public XMLEntity getXMLEntity();

    public void setXMLEntity(XMLEntity entity) throws XMLException;

    public void close();

    public int getRowIndexByFID(IFeature feat);

    public DBLayerDefinition getLyrDef();

    public String getWhereClause();

    public String[] getFields();

    public IFeatureIterator getFeatureIterator(Rectangle2D r, String strEPSG, String[] alphaNumericFieldsNeeded);

    public IFeatureIterator getFeatureIterator(Rectangle2D r, String strEPSG);
}
