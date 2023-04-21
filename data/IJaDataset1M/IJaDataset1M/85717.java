package com.icteam.fiji.manager;

import com.icteam.fiji.Service;
import com.icteam.fiji.model.TipSet;
import com.icteam.fiji.manager.exception.ConcurrentAccessException;
import com.icteam.fiji.manager.exception.CreateException;
import com.icteam.fiji.manager.exception.UpdateException;
import com.icteam.fiji.mapclustering.IPoint;
import com.icteam.fiji.mapclustering.LayerSet;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: F.PINOTTI
 * Date: 2-gen-2008
 * Time: 15.58.17
 * To change this template use File | Settings | File Templates.
 */
public interface MapManager extends Service {

    void createLayerSet(LayerSet ls) throws Exception;

    void deleteAll() throws Exception;

    LayerSet getAllLayers() throws Exception;

    LayerSet getAllLayers(TipSet tipSet) throws Exception;

    LayerSet getLayers(Long cEntBsnsAreaGeogr) throws Exception;

    LayerSet getLayers(Long cEntBsnsAreaGeogr, TipSet tipSet) throws Exception;

    Vector<IPoint> getPoints() throws Exception;
}
