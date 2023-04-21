package sample.threadCheck.predicate;

import edu.rice.cs.cunit.threadCheck.PredicateLink;

class WrongPackageProtectedPredicate {

    public static boolean check(Object thisO) {
        System.out.println("WrongPackageProtectedPredicate: thisO = " + thisO);
        return false;
    }
}

public class TCSample7 {

    public static class OKPublicStaticPredicate {

        public static boolean check(Object thisO) {
            System.out.println("OKPublicStaticPredicate: thisO = " + thisO);
            return false;
        }

        public static class OKPublicStaticPublicStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("OKPublicStaticPublicStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        protected static class WrongPublicStaticProtectedStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongPublicStaticProtectedStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        private static class WrongPublicStaticPrivateStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongPublicStaticPrivateStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        static class WrongPublicStaticStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongPublicStaticStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }
    }

    protected static class WrongProtectedStaticPredicate {

        public static boolean check(Object thisO) {
            System.out.println("WrongProtectedStaticPredicate: thisO = " + thisO);
            return false;
        }

        public static class WrongProtectedStaticPublicStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongProtectedStaticPublicStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        protected static class WrongProtectedStaticProtectedStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongProtectedStaticProtectedStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        private static class WrongProtectedStaticPrivateStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongProtectedStaticPrivateStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        static class WrongProtectedStaticStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongProtectedStaticStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }
    }

    private static class WrongPrivateStaticPredicate {

        public static boolean check(Object thisO) {
            System.out.println("WrongPrivateStaticPredicate: thisO = " + thisO);
            return false;
        }

        public static class WrongPrivateStaticPublicStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongPrivateStaticPublicStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        protected static class WrongPrivateStaticProtectedStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongPrivateStaticProtectedStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        private static class WrongPrivateStaticPrivateStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongPrivateStaticPrivateStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }

        static class WrongPrivateStaticStaticPredicate {

            public static boolean check(Object thisO) {
                System.out.println("WrongPrivateStaticStaticAnnotation: thisO = " + thisO);
                return false;
            }
        }
    }

    @PredicateLink(WrongPackageProtectedPredicate.class)
    public @interface WrongPackageProtectedAnnotation {
    }

    @PredicateLink(OKPublicStaticPredicate.class)
    public @interface OKPublicStaticAnnotation {
    }

    @PredicateLink(OKPublicStaticPredicate.OKPublicStaticPublicStaticPredicate.class)
    public @interface OKPublicStaticPublicStaticAnnotation {
    }

    @PredicateLink(OKPublicStaticPredicate.WrongPublicStaticPrivateStaticPredicate.class)
    public @interface WrongPublicStaticPrivateStaticAnnotation {
    }

    @PredicateLink(OKPublicStaticPredicate.WrongPublicStaticProtectedStaticPredicate.class)
    public @interface WrongPublicStaticProtectedStaticAnnotation {
    }

    @PredicateLink(OKPublicStaticPredicate.WrongPublicStaticStaticPredicate.class)
    public @interface WrongPublicStaticStaticAnnotation {
    }

    @PredicateLink(WrongProtectedStaticPredicate.class)
    public @interface WrongProtectedStaticAnnotation {
    }

    @PredicateLink(WrongProtectedStaticPredicate.WrongProtectedStaticPublicStaticPredicate.class)
    public @interface WrongProtectedStaticPublicStaticAnnotation {
    }

    @PredicateLink(WrongProtectedStaticPredicate.WrongProtectedStaticPrivateStaticPredicate.class)
    public @interface WrongProtectedStaticPrivateStaticAnnotation {
    }

    @PredicateLink(WrongProtectedStaticPredicate.WrongProtectedStaticProtectedStaticPredicate.class)
    public @interface WrongProtectedStaticProtectedStaticAnnotation {
    }

    @PredicateLink(WrongProtectedStaticPredicate.WrongProtectedStaticStaticPredicate.class)
    public @interface WrongProtectedStaticStaticAnnotation {
    }

    @PredicateLink(WrongPrivateStaticPredicate.class)
    public @interface WrongPrivateStaticAnnotation {
    }

    @PredicateLink(WrongPrivateStaticPredicate.WrongPrivateStaticPublicStaticPredicate.class)
    public @interface WrongPrivateStaticPublicStaticAnnotation {
    }

    @PredicateLink(WrongPrivateStaticPredicate.WrongPrivateStaticPrivateStaticPredicate.class)
    public @interface WrongPrivateStaticPrivateStaticAnnotation {
    }

    @PredicateLink(WrongPrivateStaticPredicate.WrongPrivateStaticProtectedStaticPredicate.class)
    public @interface WrongPrivateStaticProtectedStaticAnnotation {
    }

    @PredicateLink(WrongPrivateStaticPredicate.WrongPrivateStaticStaticPredicate.class)
    public @interface WrongPrivateStaticStaticAnnotation {
    }

    @WrongPackageProtectedAnnotation
    @OKPublicStaticAnnotation
    @OKPublicStaticPublicStaticAnnotation
    @WrongPublicStaticPrivateStaticAnnotation
    @WrongPublicStaticProtectedStaticAnnotation
    @WrongPublicStaticStaticAnnotation
    @WrongProtectedStaticAnnotation
    @WrongProtectedStaticPublicStaticAnnotation
    @WrongProtectedStaticPrivateStaticAnnotation
    @WrongProtectedStaticProtectedStaticAnnotation
    @WrongProtectedStaticStaticAnnotation
    @WrongPrivateStaticAnnotation
    @WrongPrivateStaticPublicStaticAnnotation
    @WrongPrivateStaticPrivateStaticAnnotation
    @WrongPrivateStaticProtectedStaticAnnotation
    @WrongPrivateStaticStaticAnnotation
    public static void main(String[] args) {
        System.out.println("main!");
    }
}
