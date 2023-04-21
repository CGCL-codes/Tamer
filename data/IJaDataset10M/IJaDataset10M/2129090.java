package org.pachyderm.foundation;

import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.CXMutableLocalizedValue;
import com.webobjects.foundation.NSTimestamp;

public abstract class PXInfoModel extends PXAbstractModel {

    public PXInfoModel() {
        super();
    }

    public PXInfoModel(PXPresentationDocument document) {
        super(document);
    }

    public abstract String _identifier();

    public abstract String _id();

    public abstract String title();

    public abstract void setTitle(String title);

    public abstract CXLocalizedValue localizedDescription();

    public abstract void setLocalizedDescription(CXLocalizedValue description);

    public String primaryDescription() {
        CXLocalizedValue description = localizedDescription();
        return (description != null) ? (String) description.primaryValue() : null;
    }

    public void setPrimaryDescription(String description) {
        CXLocalizedValue localized = localizedDescription();
        CXMutableLocalizedValue mutable = (localized != null) ? localized.mutableClone() : new CXMutableLocalizedValue();
        mutable.setPrimaryValue(description);
        setLocalizedDescription(mutable);
    }

    public abstract NSTimestamp creationDate();

    public abstract NSTimestamp modificationDate();

    public abstract void notePresentationWasModified();
}
