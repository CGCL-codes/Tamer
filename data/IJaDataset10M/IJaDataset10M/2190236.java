package jaskuss_msg_demo;

public class A {

    public jaskuss.msg.MessageDispatcher msgdspg_;

    public A() throws Exception {
        msgdspg_ = new jaskuss.msg.MessageDispatcher("Jack", "message_handler_", this);
    }

    /** The syntax of the message handler method name is:
	*
	* < prefix >< jaskuss.msg.Message.data_type_ >_< the rest of the name > 
	*
        * The prefix is the one that is described in the 
	* call to the jaskuss.msg.MessageDispatcher constructor.
        * The underscore after the ID is mandatory. The message handler
	* method must be public and the class that owns the 
	* method must also be public. */
    public void message_handler_69_ScoobyDoo(jaskuss.msg.Message msg) {
        String s = (String) (msg.data_);
        System.out.println("My name is Jack and I heard " + "somebody say: " + s);
        msgdspg_.send("Jill", 48, "Howdy.");
    }
}
