package interfaces;

import java.util.Set;
import base.enums.E_LOCTYPE;

public interface ILocation extends INameable {

    public void addLink(ILocation l_);

    public int getLength();

    public ILocation nextStepTo(ILocation objective_);

    public ILocation nextStepTo(String objective_);

    public void notifyIsNOTAt(IInteractable m_);

    public void notifyIsAt(IInteractable m_);

    public E_LOCTYPE getLocationType();

    public Set<IInteractable> getWhoIsHere();

    public Set<String> getNamesOfWhoIsHere();

    public Set<ILocation> getAdjacentLocations();
}
