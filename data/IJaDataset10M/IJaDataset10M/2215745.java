package uk.ac.cam.caret.lom.api;

import uk.ac.cam.caret.minibix.general.*;

public interface LomAnnotation extends LomObject, Reproducable {

    public VCard getPerson();

    public LomDate getDate();

    public LomMultilingualString getDescription();

    public void setDate(LomDate in);

    public void setDate(String time, LomMultilingualString description);

    public void setDescription(LomMultilingualString in);

    public void setPerson(VCard in);

    public void setPerson(String in);
}
