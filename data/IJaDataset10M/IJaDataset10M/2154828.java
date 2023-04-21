package net.sf.echobinding.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;
import net.sf.echobinding.binding.*;
import net.sf.echobinding.controls.*;
import net.sf.echobinding.validation.RegexValidator;

/**
 *
 */
public class _PresentationModelTest extends TestCase {

    private _TestAlbum _album1;

    public void testPresentationModel() {
        OgnlBindingContext ctx = new OgnlBindingContext(_album1);
        _album1.addPropertyChangeListener(ctx);
        PresentationModel pMod = new DummyPresentationModel();
        ctx.setPresentationModel(pMod);
        TextField tfAlbum = new TextField("album", ctx);
        TextField tfArtist = new TextField("artist", ctx);
        CheckBox cbIsClassical = new CheckBox("classical", ctx);
        TextField tfComposer = new TextField("composer", ctx);
        Label label = new Label("#pm.top40", ctx);
        pMod.init();
        assertEquals("(A1)", "Funeral", tfAlbum.getText());
        assertEquals("(A2)", "Arcade Fire", tfArtist.getText());
        assertFalse("(A3)", cbIsClassical.isSelected());
        assertFalse("(A4)", tfComposer.isEnabled());
        cbIsClassical.setSelected(true);
        assertTrue("(A5)", tfComposer.isEnabled());
        assertEquals("(A6)", "Top40", label.getText());
        _album1.setChartPosition(123);
        assertEquals("(A7)", "NotTop40", label.getText());
    }

    public void testPModGetSetValue() {
        BindingContext ctx = new OgnlBindingContext();
        DummyPresentationModel pmod = new DummyPresentationModel();
        ctx.setPresentationModel(pmod);
        ctx.add("prop", new OgnlPropertyAdapter("#pm.prop"));
        assertNotNull(ctx.getValue("prop"));
        ctx.setValue("prop", "some value");
        assertEquals("some value", pmod.getProp());
    }

    public void testPModAndValidation() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String datePattern = "([\\d]{2}).([\\d]{2}).([\\d]{2,4})";
        _TestAlbum album = new _TestAlbum("foo", "bar", null, false);
        album.setReleaseDate(sdf.parse("01.01.2001"));
        OgnlBindingContext ctx = new OgnlBindingContext(album);
        DummyPresentationModel pmod = new DummyPresentationModel();
        ctx.setPresentationModel(pmod);
        OgnlPropertyAdapter releaseDateStringAdapter = new OgnlPropertyAdapter("#pm.releaseDateString");
        releaseDateStringAdapter.addValidator(new RegexValidator(datePattern));
        ctx.add("releaseDateString", releaseDateStringAdapter);
        assertTrue("P1", ctx.isValid());
        Date releaseDate = (Date) ctx.getValue("releaseDate");
        String releaseDateString = (String) ctx.getValue("releaseDateString");
        assertEquals("P2", sdf.parse(releaseDateString), releaseDate);
        assertTrue("P2.1", ctx.validate("releaseDateString", "01.01.2004").isValid());
        assertFalse("P2.2", ctx.validate("releaseDateString", "x1,01.2004").isValid());
        TextField tfReleaseDate = new TextField("releaseDateString", ctx);
        assertEquals("P3", "01.01.2001", tfReleaseDate.getText());
        tfReleaseDate.setText("x1,01.2001");
        assertFalse("P4", tfReleaseDate.isValid());
        assertFalse("P5", ctx.isValid());
        album.setReleaseDate(null);
        Exception e = null;
        try {
            ctx.getValue("releaseDateString");
        } catch (Exception ex) {
            e = ex;
        }
        assertNotNull(e);
    }

    protected void setUp() throws Exception {
        super.setUp();
        _album1 = new _TestAlbum("Funeral", "Arcade Fire", null, false);
        _album1.setChartPosition(23);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

class DummyPresentationModel extends AbstractPresentationModel {

    public void configure() {
        getControl("classical").addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                getControl("composer").setEnabled((Boolean) event.getNewValue());
            }
        });
        getBean().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("chartPosition".equals(evt.getPropertyName())) {
                    getControl("#pm.top40").update();
                }
            }
        });
    }

    _TestAlbum getBean() {
        return (_TestAlbum) getContext().getModel();
    }

    public String getTop40() {
        if (getBean().isClassical()) return "Classical";
        if (getBean().getChartPosition() <= 40) return "Top40";
        return "NotTop40";
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public String getReleaseDateString() {
        Date releaseDate = (Date) getValue("releaseDate");
        return sdf.format(releaseDate);
    }

    public void setReleaseDateString(String releaseDate) {
        Date date = null;
        try {
            date = sdf.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setValue("releaseDate", date);
    }

    public PresentationModel cloneInstance() {
        return null;
    }

    String _prop = "foo";

    /**
	 * @return Returns the prop.
	 */
    public String getProp() {
        return _prop;
    }

    /**
	 * @param prop The prop to set.
	 */
    public void setProp(String prop) {
        _prop = prop;
    }
}
