package jade.core;

/**
   This interface is implemented by all the
   service helper classes, i.e. SecurityHelper, etc...
   Agents get their service helper by the method: Agent.getHelper(String)

   @see jade.core.Agent#getHelper

*/
public interface ServiceHelper {

    public void init(Agent a);
}
