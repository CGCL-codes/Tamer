package test;

class simpleThread3 {

    public static void main(String[] dummy) {
        ThreadGroup myGroup1 = new ThreadGroup("A");
        ThreadGroup myGroup2 = new ThreadGroup(myGroup1, "B");
        ThreadGroup myGroup3 = new ThreadGroup(myGroup1, "C");
        TestRunnable myRunnable = new TestRunnable();
        Thread myThread1 = new Thread(myGroup1, myRunnable);
        Thread myThread2 = new Thread(myGroup2, myRunnable);
        Thread myThread3 = new Thread(myGroup2, myRunnable);
        myThread1.setName("1");
        myThread2.setName("2");
        myThread3.setName("3");
        myThread1.start();
        myThread2.start();
        myThread3.start();
        int pri = myGroup1.getMaxPriority();
        myGroup2.setMaxPriority(pri);
        System.out.println(myGroup1.getName());
        System.out.println(myGroup2.getName());
        System.out.println(myGroup3.getName());
        System.out.println(myThread1.getThreadGroup().getName());
        System.out.println(myThread2.getThreadGroup().getName());
        System.out.println(myThread3.getThreadGroup().getName());
        System.out.println(myThread1.activeCount());
        System.out.println(myThread2.activeCount());
        System.out.println(myThread3.activeCount());
        if (myGroup1.isDaemon() == false) {
            System.out.println("myGroup1 is not Daemon");
            myGroup2.setDaemon(true);
            System.out.println("myGroup2 set Daemon");
        }
        System.out.print(myGroup2.getName());
        System.out.print("->");
        System.out.println(myGroup2.getParent());
        if (myGroup1.parentOf(myGroup2)) {
            System.out.println("A<-B");
        }
        int temp1 = myGroup1.activeGroupCount();
        System.out.print(temp1);
        System.out.println(" : myGroup1 active group count");
        int temp2 = myGroup2.activeCount();
        System.out.print(temp2);
        System.out.println(" : myGroup2 active count");
        myGroup2.interrupt();
        System.out.println("myGroup2 interrupt");
        if (myGroup2.isDestroyed() == false) {
            System.out.println("myGroup3 not destroyed");
            myGroup2.destroy();
            System.out.println("  destroy it");
        }
        ThreadGroup[] list1 = new ThreadGroup[5];
        Thread[] list2 = new Thread[5];
        int gCount, tCount;
        gCount = myGroup1.enumerate(list1);
        System.out.print(gCount);
        System.out.println(" : enum myGroup1");
        for (int i = 0; i < gCount; i++) {
            System.out.print("Item ");
            System.out.print(i);
            System.out.print(" is ");
            System.out.println(list1[i].getName());
        }
        gCount = myGroup1.enumerate(list1, true);
        System.out.print(gCount);
        System.out.println(" : enum myGroup1 recurse");
        for (int i = 0; i < gCount; i++) {
            System.out.print("Item ");
            System.out.print(i);
            System.out.print(" is ");
            System.out.println(list1[i].getName());
        }
        tCount = myGroup2.enumerate(list2);
        System.out.print(tCount);
        System.out.println(" : enum myGroup2");
        for (int i = 0; i < tCount; i++) {
            System.out.print("Item ");
            System.out.print(i);
            System.out.print(" is ");
            System.out.println(list2[i].getName());
        }
        tCount = myGroup2.enumerate(list2, true);
        System.out.print(tCount);
        System.out.println(" : enum myGroup2 recurse");
        for (int i = 0; i < tCount; i++) {
            System.out.print("Item ");
            System.out.print(i);
            System.out.print(" is ");
            System.out.println(list2[i].getName());
        }
    }
}

class TestRunnable implements Runnable {

    public void run() {
        while (true) {
            Thread.sleep(1000);
        }
    }
}
