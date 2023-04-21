package foo.bar.hello.world;

class Temp {
}

public class InnerClasses extends Temp {

    Object supportLocalBusiness() {
        final int x = 54;
        class FooBar {

            public int hashCode() {
                return x;
            }
        }
        return new FooBar();
    }

    static Object anonymousCoward() {
        final int x = 5;
        return new Object() {

            public int hashCode() {
                return x;
            }
        };
    }

    static String xxxx = "odd";

    public static void main(String args[]) {
        new Outie("eenie").new Innie().meenie();
        System.out.println(anonymousCoward().hashCode());
        final String xx = "crazy";
        Outie outie = new Outie("weird") {

            public String toString() {
                return "bogus" + x + xx + xxxx;
            }
        };
        System.out.println(outie);
        ABC a = new ABC("hello") {
        };
        System.out.println(new InnerClasses().supportLocalBusiness().hashCode());
        System.out.println(a);
        System.out.println(new SuperEnclosing().new SubEnclosed().hello());
        System.out.println(new SuperEnclosing().new SubEnclosed().seVar);
        System.out.println(new SuperEnclosing().new SubEnclosed().hello2());
        System.out.println(new SuperEnclosing().new SubEnclosed().seVar2);
        SuperEnclosing2 se2 = new SuperEnclosing2();
        SuperEnclosing2.SubEnclosed2 sub2 = se2.new SubEnclosed2();
        System.out.println(sub2.hello());
        sub2.setSEVar();
        System.out.println(sub2.hello());
        se2.setSEVar();
        System.out.println(sub2.hello());
        int foo = 12;
        foo++;
        --foo;
    }
}

class ABC {

    ABC(Object x) {
    }
}

class Outie {

    String x;

    Outie(String s) {
        x = s;
    }

    class Innie {

        public void meenie() {
            System.out.println(x);
        }
    }
}

class SuperEnclosing {

    int seVar = 6;

    int seVar2 = 10;

    class SubEnclosed extends SuperEnclosing {

        int seVar2 = 11;

        SubEnclosed() {
            this.seVar = 5;
            SuperEnclosing.this.seVar = 7;
            this.seVar2 = 12;
            SuperEnclosing.this.seVar2 = 13;
        }

        int hello() {
            return seVar;
        }

        int helloAgain() {
            return SubEnclosed.this.seVar;
        }

        int hello2() {
            return seVar2;
        }
    }
}

class SuperEnclosing2 {

    private int seVar = 6;

    public void setSEVar() {
        seVar = 1001;
    }

    class SubEnclosed2 extends SuperEnclosing2 {

        SubEnclosed2() {
            SuperEnclosing2.this.seVar += -(-(+7));
        }

        int hello() {
            return seVar;
        }
    }
}
