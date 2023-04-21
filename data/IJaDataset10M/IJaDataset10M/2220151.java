package org.xsocket.server.smtp;

public final class BulkTestLargeMailSender {

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        System.out.println("start test client for server " + args[0] + ":" + args[1] + " with " + args[2] + " threads");
        int size = Integer.parseInt(args[2]);
        for (int i = 0; i < size; i++) {
            Thread t = new Thread() {

                public void run() {
                    try {
                        LargeMailSender sender = new LargeMailSender();
                        while (true) {
                            boolean success = sender.send(args[0], Integer.parseInt(args[1]));
                            if (success) {
                                System.out.print(".");
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                ;
            };
            t.start();
        }
    }
}
