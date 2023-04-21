
package JP.CO.gram.p2p.app;

import java.util.*;

import JP.CO.gram.p2p.*;

import org.jnutella.jppp.core.*;
import org.jnutella.jppp.profile.*;
import org.jnutella.jppp.routing.*;

public class Jnutella {
    
    public static void main(String[] args){    
        (new Jnutella()).start();
    }
    
    Place place = null;
    
    public void start(){
        place = new PlaceImpl(new PlaceImpl.RouterFactoryImpl());
        try {
            ProfileDescriptor desc = new MyProfileDesc();
            place.getProfileManager().installProfile(desc);
            place.start();
        } catch( org.jnutella.jppp.core.StartupException ex ){
            ex.printStackTrace();
        }
    }
        
    public class MyProfileDesc implements ProfileDescriptor {
        public String getAuthor(){
            return "umeda";
        }
        
        public String getProfileID(){
            return "JPPP/JNUTELLA";
        }
        
        public String getVersion(){
            return "0.1";
        }
        
        public int getDispatchMode(){
            return ProfileDescriptor.TARGET_MESSAGES;
        }
        
        public String[] getSupportedSelectors(){
            return new String[]{ "ping", "pong", "query", "queryhit", "push", "shortmsg" };
        }
        
        public MessageProcessor getProcessor(){
            return new MyMessageProcessor();
        }
    }
    
    
    
    public class MyMessageProcessor implements MessageProcessor {
        Hashtable messages = new Hashtable();
        Hashtable pings = new Hashtable();

        public MyMessageProcessor(){ 

        }
        
        public void processMessage(Message msg, Node sender){
            String method = msg.getSelector();
            ID id = new ID(msg.getMessageID());
            String idStr = id.toString();
            
            if( method.equals("ping" ) ){
            
                Message ping = (Message)messages.get(idStr);
                if( ping != null ) {
                    System.out.println("found the same ping");
                    return;
                }
                
                messages.put(idStr, msg);
                pings.put(idStr, sender);
                
                BasicMessage pong = new BasicMessage("pong", new byte[0]);
                pong.setDestinationID( msg.origin().getBytes() );
                pong.setMessageID( msg.getMessageID() );
                
                try {
                    place.getRouter().forward(sender, pong, new ForwarderImpl(Forwarder.NEVER_FORWARD));
                } catch( Exception ex ){ ex.printStackTrace(); }
            
            } else if( method.equals("pong" ) ){

                messages.put(idStr+"_pong", msg);
                
                Identity dest = msg.destination();
                if( place.getID().equals(dest) ){
                    System.out.println("***** yes!! I catched the anwer of my ping *****");
                    System.out.println(new String(msg.getPayload()));
                    return;
                }
                
                Node pingOrigin = (Node)pings.get(idStr);
                if( PlaceImpl.DEBUG )
                    System.out.println("finding ping origin... "+idStr+" "+pingOrigin+" "+sender+" "+pings.size());
                Enumeration enum = pings.keys();
                while( enum.hasMoreElements() ){
                    String str = (String)enum.nextElement();
                    System.out.println(str+ " " +pings.get(str));
                }
                    
                    
                if( pingOrigin != null && ! pingOrigin.equals(sender) ){
                    if( PlaceImpl.DEBUG )
                        System.out.println("OK, forward the pong message to "+pingOrigin);
                        
                    try {
                        place.getRouter().forward(pingOrigin, msg, new ForwarderImpl(Forwarder.NEVER_FORWARD));
                    } catch( Exception ex ){ ex.printStackTrace(); }
                }
                
            } else if( method.equals("query" ) ){
                return;
            } else if( method.equals("queryhit" ) ){
                return;
            } else if( method.equals("push" ) ){
                return;
            } else if( method.equals("shortmsg") ){
                System.out.println(new String(msg.getPayload()));
            }
            return;
        }
    }
}