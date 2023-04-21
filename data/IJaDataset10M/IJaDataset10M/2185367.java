package org.fudaa.ebli.dialog;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import javax.swing.JComponent;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuInformationsDocument;
import org.fudaa.ebli.impression.EbliPageFormat;
import org.fudaa.ebli.impression.EbliPageable;
import org.fudaa.ebli.impression.EbliPageableDelegate;

/**
 * @version      $Id: BDialogContentImprimable.java,v 1.9 2006-09-19 14:55:56 deniger Exp $
 * @author       Fred Deniger
 */
public abstract class BDialogContentImprimable extends BDialogContent implements EbliPageable {

    protected BuInformationsDocument id_;

    private EbliPageableDelegate delegueImpression_;

    public BDialogContentImprimable(final BuCommonInterface _app, final String _title, final BuInformationsDocument _id) {
        super(_app, _title);
        id_ = _id;
    }

    public BDialogContentImprimable(final BuCommonInterface _app, final String _title, final JComponent _message, final BuInformationsDocument _id) {
        super(_app, _title, _message);
        id_ = _id;
    }

    public BDialogContentImprimable(final BuCommonInterface _app, final BDialogContent _parent, final String _title, final BuInformationsDocument _id) {
        super(_app, _parent, _title);
        id_ = _id;
    }

    public BDialogContentImprimable(final BuCommonInterface _app, final BDialogContent _parent, final String _title, final JComponent _message, final BuInformationsDocument _id) {
        super(_app, _parent, _title, _message);
        id_ = _id;
    }

    public void setInformationsDocument(final BuInformationsDocument _id) {
        id_ = _id;
    }

    public final Printable getPrintable(final int _i) {
        if (delegueImpression_ == null) {
            delegueImpression_ = new EbliPageableDelegate(this);
        }
        return delegueImpression_.getPrintable(_i);
    }

    public final PageFormat getPageFormat(final int _i) {
        if (delegueImpression_ == null) {
            delegueImpression_ = new EbliPageableDelegate(this);
        }
        return delegueImpression_.getPageFormat(_i);
    }

    public final EbliPageFormat getDefaultEbliPageFormat() {
        if (delegueImpression_ == null) {
            delegueImpression_ = new EbliPageableDelegate(this);
        }
        return delegueImpression_.getDefaultEbliPageFormat();
    }

    public BuInformationsDocument getInformationsDocument() {
        if (delegueImpression_ == null) {
            delegueImpression_ = new EbliPageableDelegate(this);
        }
        return id_;
    }
}
