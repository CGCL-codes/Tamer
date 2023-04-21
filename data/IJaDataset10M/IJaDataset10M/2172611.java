package org.openorb.orb.examples.dynany;

public final class Client {

    private Client() {
    }

    public static void main(String[] args) {
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);
        org.omg.CORBA.Object obj = null;
        try {
            java.io.FileInputStream file = new java.io.FileInputStream("ObjectId");
            java.io.InputStreamReader myInput = new java.io.InputStreamReader(file);
            java.io.BufferedReader reader = new java.io.BufferedReader(myInput);
            String ref = reader.readLine();
            obj = orb.string_to_object(ref);
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
        try {
            IPolymorph poly = IPolymorphHelper.narrow(obj);
            org.omg.CORBA.Any any = orb.create_any();
            System.out.println("Sending boolean...");
            any.insert_boolean(true);
            poly.pass(any);
            System.out.println("Sending long...");
            any.insert_long(123);
            poly.pass(any);
            System.out.println("Sending String...");
            any.insert_string(" Hello world ! ");
            poly.pass(any);
            System.out.println("Sending class 'person'...");
            person p = new person(" John ", " Bob ");
            personHelper.insert(any, p);
            poly.pass(any);
            System.out.println("Sending int sequence...");
            int[] seq = new int[10];
            for (int i = 0; i < seq.length; i++) {
                seq[i] = i;
            }
            longSeqHelper.insert(any, seq);
            poly.pass(any);
            System.out.println("Sending float...");
            any.insert_float((float) 3.14);
            System.out.println(" We are going to catch an exception... ");
            poly.pass(any);
        } catch (UnknownType ex) {
            System.out.println(" A unknown type has been sent to the server. ");
        } catch (org.omg.CORBA.SystemException ex) {
            ex.printStackTrace();
        }
    }
}
