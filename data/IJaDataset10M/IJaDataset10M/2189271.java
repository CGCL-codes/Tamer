package org.dcm4chee.xero.wado.multi;

import static org.dcm4chee.xero.wado.WadoParams.*;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.dcm4chee.xero.metadata.filter.FilterItem;
import org.dcm4chee.xero.metadata.servlet.ServletResponseItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FrameNumberItFilterTest {

    FrameNumberItFilter filter = new FrameNumberItFilter();

    Map<String, Object> params;

    FilterItem<Iterator<ServletResponseItem>> filterItem;

    List<ServletResponseItem> l1, l3;

    ServletResponseItem sri1a, sri1b, sri3;

    Iterator<ServletResponseItem> it1, it2, it3;

    Filter<DicomObject> dicomFilter;

    static ClassLoader cl = Thread.currentThread().getContextClassLoader();

    /** Read a dicom object input stream */
    public static DicomObject dicomResource(String resourceName) {
        URL url = cl.getResource(resourceName);
        if (url == null) throw new IllegalArgumentException("Unknown resource " + resourceName);
        DicomInputStream dis;
        try {
            dis = new DicomInputStream(url.openStream());
            return dis.readDicomObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Problem reading DICOM input stream:" + e);
        }
    }

    @BeforeMethod
    @SuppressWarnings("unchecked")
    void init() {
        params = new HashMap<String, Object>();
        filterItem = createMock(FilterItem.class);
        l1 = new ArrayList<ServletResponseItem>();
        l3 = new ArrayList<ServletResponseItem>();
        sri1a = createMock(ServletResponseItem.class);
        sri1b = createMock(ServletResponseItem.class);
        l1.add(sri1a);
        l1.add(sri1b);
        sri3 = createMock(ServletResponseItem.class);
        l3.add(sri3);
        it1 = l1.iterator();
        it2 = null;
        it3 = l3.iterator();
        dicomFilter = createMock(Filter.class);
        filter.setDicomImageHeader(dicomFilter);
    }

    @Test
    public void testFilter_withMissingObject_andReturnNull() {
        params.put(OBJECT_UID, "1.2.3");
        expect(dicomFilter.filter(null, params)).andReturn(null);
        replay(dicomFilter);
        assert filter.filter(null, params) == null;
        verify(dicomFilter);
    }

    @Test
    public void testFilter_withSingleFrameObject_andReturnFrame() {
        params.put(OBJECT_UID, "1.2.3");
        DicomObject ds = dicomResource("misc/pixelPadding.dcm");
        expect(dicomFilter.filter(null, params)).andReturn(ds);
        expect(filterItem.callNextFilter(params)).andReturn(it1);
        replay(dicomFilter, filterItem);
        Iterator<ServletResponseItem> it = filter.filter(filterItem, params);
        assert it == it1;
        verify(dicomFilter);
    }

    @Test
    public void testFilter_withMultiFrameObject_andReturnAllFrames() {
        params.put(OBJECT_UID, "1.2.3");
        DicomObject ds = dicomResource("imgconsistency/cplx_p02.dcm");
        expect(dicomFilter.filter(null, params)).andReturn(ds);
        expect(filterItem.callNextFilter(params)).andReturn(it1);
        expect(filterItem.callNextFilter(params)).andReturn(it3);
        replay(dicomFilter, filterItem);
        Iterator<ServletResponseItem> it = filter.filter(filterItem, params);
        assert it != null;
        assert it.hasNext();
        assert it.next() == sri1a;
        assert it.hasNext();
        assert it.next() == sri1b;
        assert it.hasNext();
        assert it.next() == sri3;
        assert !it.hasNext();
        verify(dicomFilter);
    }

    @Test
    public void testFilter_withSimpleFrameList_andReturnSelectedFrames() {
        params.put(OBJECT_UID, "1.2.3");
        params.put(SIMPLE_FRAME_LIST, "2-4,7");
        DicomObject ds = dicomResource("imgconsistency/cplx_p02.dcm");
        expect(dicomFilter.filter(null, params)).andReturn(ds);
        expect(filterItem.callNextFilter(params)).andReturn(it3);
        replay(dicomFilter, filterItem);
        Iterator<ServletResponseItem> it = filter.filter(filterItem, params);
        assert it != it3;
        assert it.hasNext();
        assert it.next() == sri3;
        assert !it.hasNext();
        verify(dicomFilter);
    }
}
