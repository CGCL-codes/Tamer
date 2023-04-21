package net.sf.brightside.aikido.metamodel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import net.sf.brightside.aikido.metamodel.beans.AikidoistIsAlreadyRegistredException;

public interface Practice {

    public Date getDate();

    public void setDate(Date date);

    public List<Aikidoist> getAikidoists();

    public Aikidoist getSensei();

    public void setSensei(Aikidoist sensei);

    public Dojo getDojo();

    public void setDojo(Dojo dojo);

    public List<RankExam> getRankExams();

    public Serializable takeId();

    public Aikidoist addAikidoist(Aikidoist aikidoist) throws AikidoistIsAlreadyRegistredException;

    public String dateFormatted();
}
