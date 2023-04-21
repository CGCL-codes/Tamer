package gov.sns.tools.da;

import java.util.*;

/**
 *  The collection of the static methods to perform different operations on Tps
 *  objects.
 *
 *@author     shishlo
 *created    November 8, 2006
 */
public class TpsOperations {

    private static Integer[] intArr = IntegerArray.getArray();

    /**
	 *  Calculates multiplication of the two Tps objects and put result in the
	 *  first Tps object.
	 *
	 *@param  tps1                        The Tps object that will be changed.
	 *@param  tps2                        The constant Tps object.
	 *@exception  TpsOperationsException  The Exception
	 */
    public static void mult(Tps tps1, Tps tps2) throws TpsOperationsException {
        if (tps1.isNumber()) {
            if (tps1.getParent() != null) {
                System.err.println("===== TpsOperations class.mult(Tps tps1, Tps tps2)==========");
                System.err.println("This method can not handle Tps number that have a parent!");
                System.err.println("Tps tps1=" + tps1.toString());
                System.err.println("Tps tps2=" + tps2.toString());
                throw new TpsOperationsException();
            }
            if (tps2.isNumber()) {
                tps1.getNumber().mult(tps2.getNumber());
                tps1.setOrder(tps1.getOrder() + tps2.getOrder());
            } else {
                double re = tps1.getNumber().getRe();
                double im = tps1.getNumber().getIm();
                tps1.getNumber().set(0., 0.);
                Iterator<Tps> itr = tps2.getTpsCollection().iterator();
                while (itr.hasNext()) {
                    Tps tps_in = itr.next();
                    Tps tps = TpsOperations.copy(tps_in);
                    TpsOperations.mult(tps, re, im);
                    tps.setOrder(tps.getOrder() + tps1.getOrder());
                    tps1.add(tps);
                }
                tps1.setOrder(0);
            }
        } else if (tps2.isNumber()) {
            double re = tps2.getNumber().getRe();
            double im = tps2.getNumber().getIm();
            Collection<Tps> coll = tps1.removeAllChildren();
            Iterator<Tps> itr = coll.iterator();
            while (itr.hasNext()) {
                Tps tps = itr.next();
                TpsOperations.mult(tps, re, im);
                tps.setOrder(tps.getOrder() + tps2.getOrder());
                tps1.add(tps);
            }
        } else {
            Integer[] intArr = IntegerArray.getArray();
            Hashtable<Integer, Tps> tps_ht = new Hashtable<Integer, Tps>();
            Collection<Tps> coll_1 = tps1.getTpsCollection();
            Collection<Tps> coll_2 = tps2.getTpsCollection();
            Iterator<Tps> itr_1 = coll_1.iterator();
            Iterator<Tps> itr_2;
            while (itr_1.hasNext()) {
                Tps tps_1 = itr_1.next();
                int ord_1 = tps_1.getOrder();
                itr_2 = coll_2.iterator();
                while (itr_2.hasNext()) {
                    Tps tps_2 = itr_2.next();
                    int ord_2 = tps_2.getOrder();
                    int ord = ord_1 + ord_2;
                    Integer o = intArr[ord];
                    Tps tps = null;
                    if (tps_ht.containsKey(o)) {
                        tps = (Tps) tps_ht.get(o);
                    } else {
                        tps = TpsOperations.getTps();
                        tps.setOrder(ord);
                        tps_ht.put(o, tps);
                    }
                    Tps tps_tmp = TpsOperations.copy(tps_1);
                    if (tps_tmp.isNumber() || tps_2.isNumber()) {
                        multTpsAndNumber(tps_tmp, tps_2);
                    } else {
                        TpsOperations.mult(tps_tmp, tps_2);
                    }
                    TpsOperations.add(tps, tps_tmp);
                    TpsOperations.removeTps(tps_tmp);
                }
            }
            Collection<Tps> coll_toRemove = tps1.removeAllChildren();
            TpsOperations.removeTps(coll_toRemove);
            Collection<Tps> coll = tps_ht.values();
            Iterator<Tps> itr = coll.iterator();
            while (itr.hasNext()) {
                tps1.add(itr.next());
            }
        }
    }

    /**
	 *  Multiply two Tps objects when at least one of them is a Number. This method
	 *  will not change orders of the Tps objects. It is assumed that orders
	 *  assignment will be handled by mult(Tps,Tps) method.
	 *
	 *@param  tps1  The Tps object that will be changed.
	 *@param  tps2  The constant Tps object.
	 */
    private static void multTpsAndNumber(Tps tps1, Tps tps2) {
        if (tps1.isNumber()) {
            if (tps2.isNumber()) {
                tps1.getNumber().mult(tps2.getNumber());
            } else {
                double re = tps1.getNumber().getRe();
                double im = tps1.getNumber().getIm();
                tps1.getNumber().set(0., 0.);
                Iterator<Tps> itr = tps2.getTpsCollection().iterator();
                while (itr.hasNext()) {
                    Tps tps_in = itr.next();
                    Tps tps = TpsOperations.copy(tps_in);
                    TpsOperations.mult(tps, re, im);
                    tps1.add(tps);
                }
            }
        } else if (tps2.isNumber()) {
            double re = tps2.getNumber().getRe();
            double im = tps2.getNumber().getIm();
            Iterator<Tps> itr = tps1.getTpsCollection().iterator();
            while (itr.hasNext()) {
                Tps tps = itr.next();
                TpsOperations.mult(tps, re, im);
            }
        }
    }

    /**
	 *  Calculates multiplication of the two Tps objects and put result in the
	 *  first Tps object. The resulting maximal order will not exceed the order_max
	 *  input parameter.
	 *
	 *@param  tps1                        The Tps object that will be changed
	 *@param  tps2                        The constant Tps object.
	 *@param  order_max                   The maximal order in the result Tps
	 *@exception  TpsOperationsException  The Exception
	 */
    public static void mult(Tps tps1, Tps tps2, int order_max) throws TpsOperationsException {
        if (tps1.isNumber()) {
            if (tps1.getParent() != null) {
                System.err.println("===== TpsOperations class.mult(Tps tps1, Tps tps2)==========");
                System.err.println("This method can not handle Tps number that have a parent!");
                System.err.println("Tps tps1=" + tps1.toString());
                System.err.println("Tps tps2=" + tps2.toString());
                throw new TpsOperationsException();
            }
            if (tps2.isNumber()) {
                int ord = tps1.getOrder() + tps2.getOrder();
                if (ord <= order_max) {
                    tps1.getNumber().mult(tps2.getNumber());
                    tps1.setOrder(ord);
                } else {
                    tps1.getNumber().set(0., 0.);
                    tps1.setOrder(0);
                }
            } else {
                double re = tps1.getNumber().getRe();
                double im = tps1.getNumber().getIm();
                tps1.getNumber().set(0., 0.);
                Iterator<Tps> itr = tps2.getTpsCollection().iterator();
                while (itr.hasNext()) {
                    Tps tps_in = itr.next();
                    Tps tps = TpsOperations.copy(tps_in);
                    TpsOperations.mult(tps, re, im);
                    int ord = tps.getOrder() + tps1.getOrder();
                    if (ord <= order_max) {
                        tps.setOrder(ord);
                        tps1.add(tps);
                    }
                }
                tps1.setOrder(0);
            }
        } else if (tps2.isNumber()) {
            double re = tps2.getNumber().getRe();
            double im = tps2.getNumber().getIm();
            Collection<Tps> coll = tps1.removeAllChildren();
            Iterator<Tps> itr = coll.iterator();
            while (itr.hasNext()) {
                Tps tps = itr.next();
                int ord = tps.getOrder() + tps2.getOrder();
                if (ord <= order_max) {
                    TpsOperations.mult(tps, re, im);
                    tps.setOrder(ord);
                    tps1.add(tps);
                }
            }
        } else {
            Integer[] intArr = IntegerArray.getArray();
            Hashtable<Integer, Tps> tps_ht = new Hashtable();
            Collection<Tps> coll_1 = tps1.getTpsCollection();
            Collection<Tps> coll_2 = tps2.getTpsCollection();
            Iterator<Tps> itr_1 = coll_1.iterator();
            Iterator<Tps> itr_2;
            while (itr_1.hasNext()) {
                Tps tps_1 = itr_1.next();
                int ord_1 = tps_1.getOrder();
                itr_2 = coll_2.iterator();
                while (itr_2.hasNext()) {
                    Tps tps_2 = itr_2.next();
                    int ord_2 = tps_2.getOrder();
                    int ord = ord_1 + ord_2;
                    if (ord <= order_max) {
                        Integer o = intArr[ord];
                        Tps tps = null;
                        if (tps_ht.containsKey(o)) {
                            tps = tps_ht.get(o);
                        } else {
                            tps = TpsOperations.getTps();
                            tps.setOrder(ord);
                            tps_ht.put(o, tps);
                        }
                        Tps tps_tmp = TpsOperations.copy(tps_1);
                        if (tps_tmp.isNumber() || tps_2.isNumber()) {
                            multTpsAndNumber(tps_tmp, tps_2);
                        } else {
                            TpsOperations.mult(tps_tmp, tps_2, order_max - ord);
                        }
                        TpsOperations.add(tps, tps_tmp);
                        TpsOperations.removeTps(tps_tmp);
                    }
                }
            }
            Collection<Tps> coll_toRemove = tps1.removeAllChildren();
            TpsOperations.removeTps(coll_toRemove);
            Collection<Tps> coll = tps_ht.values();
            Iterator<Tps> itr = coll.iterator();
            while (itr.hasNext()) {
                tps1.add(itr.next());
            }
        }
    }

    /**
	 *  Calculates multiplication of the Tps object and a number.
	 *
	 *@param  tps  The Tps object that will be changed.
	 *@param  re   The real part of the number
	 *@param  im   The imaginary part of the number
	 */
    public static void mult(Tps tps, double re, double im) {
        if (tps.isNumber()) {
            tps.getNumber().mult(re, im);
        } else {
            Iterator<Tps> itr = tps.getTpsCollection().iterator();
            while (itr.hasNext()) {
                TpsOperations.mult(itr.next(), re, im);
            }
        }
    }

    /**
	 *  Calculates multiplication of the Tps object and a number.
	 *
	 *@param  tps  The Tps object that will be changed.
	 *@param  d    The double number
	 */
    public static void mult(Tps tps, double d) {
        TpsOperations.mult(tps, d, 0.);
    }

    /**
	 *  Calculates multiplication of the Tps object and a number.
	 *
	 *@param  tps  The Tps object that will be changed.
	 *@param  i    The integer number
	 */
    public static void mult(Tps tps, int i) {
        TpsOperations.mult(tps, (double) i, 0.);
    }

    /**
	 *  This method will add tps2 to tps1. The tps2 object will not be touched.
	 *
	 *@param  tps1                        The Tps object will be changed
	 *@param  tps2                        The Tps object that will be added
	 *@exception  TpsOperationsException  The Exception
	 */
    public static void add(Tps tps1, Tps tps2) throws TpsOperationsException {
        if (tps1.isNumber() || tps2.isNumber()) {
            if (tps1.isNumber()) {
                if (tps2.isNumber()) {
                    tps1.getNumber().add(tps2.getNumber());
                } else {
                    Tps tps = TpsOperations.getTps();
                    tps.setOrder(0);
                    tps.getNumber().set(tps1.getNumber());
                    tps1.add(tps);
                    TpsOperations.add(tps1, tps2);
                }
            } else {
                if (tps2.isNumber()) {
                    Tps tps = TpsOperations.getTps();
                    tps.setOrder(0);
                    tps.getNumber().set(tps2.getNumber());
                    tps2.add(tps);
                    TpsOperations.add(tps1, tps2);
                } else {
                    System.err.println("===== TpsOperations.add(tps1,tps2) =====");
                    System.err.println("Here both Tps instances are not numbers!");
                    System.err.println("That is nonsense!");
                    System.err.println("Stop!");
                    throw new TpsOperationsException();
                }
            }
        } else {
            ArrayList<Integer> orders1 = tps1.getOrders();
            ArrayList<Integer> orders2 = tps2.getOrders();
            Iterator<Integer> itr2 = orders2.iterator();
            while (itr2.hasNext()) {
                Integer o = itr2.next();
                if (orders1.contains(o)) {
                    Tps sub_tps1 = tps1.getTps(o);
                    Tps sub_tps2 = tps2.getTps(o);
                    TpsOperations.add(sub_tps1, sub_tps2);
                } else {
                    Tps sub_tps = TpsOperations.copy(tps2.getTps(o));
                    tps1.add(sub_tps);
                }
            }
        }
    }

    /**
	 *  This method will add a number to tps1.
	 *
	 *@param  tps  The Tps object will be changed
	 *@param  re   The real part of the number
	 *@param  im   The imaginary part of the number
	 */
    public static void add(Tps tps, double re, double im) {
        Tps tps1 = TpsOperations.getTps();
        tps1.setOrder(0);
        tps1.getNumber().set(re, im);
        TpsOperations.add(tps, tps1);
        TpsOperations.removeTps(tps1);
    }

    /**
	 *  This method will add a number to tps1.
	 *
	 *@param  tps  The Tps object that will be changed.
	 *@param  d    The double number
	 */
    public static void add(Tps tps, double d) {
        Tps tps1 = TpsOperations.getTps();
        tps1.setOrder(0);
        tps1.getNumber().set(d);
        TpsOperations.add(tps, tps1);
        TpsOperations.removeTps(tps1);
    }

    /**
	 *  This method will add a number to tps1.
	 *
	 *@param  tps  The Tps object that will be changed
	 *@param  i    The integer number
	 */
    public static void add(Tps tps, int i) {
        Tps tps1 = TpsOperations.getTps();
        tps1.setOrder(0);
        tps1.getNumber().set(i);
        TpsOperations.add(tps, tps1);
        TpsOperations.removeTps(tps1);
    }

    /**
	 *  This method calculates the derivative of the Tps in place.
	 *
	 *@param  tps                         The Tps object that will be changed.
	 *@param  var_index                   The variable index
	 *@exception  TpsOperationsException  The Exception
	 */
    public static void diff(Tps tps, int var_index) throws TpsOperationsException {
        if (tps.isNumber()) {
            if (tps.getParent() != null) {
                System.err.println("===== TpsOperations class.diff(Tps tps, int var_index)==========");
                System.err.println("This method can not handle Tps number that have a parent!");
                System.err.println("Tps tps=" + tps.toString());
                throw new TpsOperationsException();
            }
            if (var_index != 0) {
                tps.setOrder(0);
                tps.getNumber().set(0., 0.);
            } else {
                if (tps.getOrder() == 0) {
                    tps.getNumber().set(0., 0.);
                } else {
                    int ord = tps.getOrder();
                    tps.getNumber().mult(ord);
                    tps.setOrder(ord - 1);
                }
            }
        } else {
            int maxDepth = tps.getMaxDepth();
            Collection<Tps> coll = tps.removeAllChildren();
            if (maxDepth < var_index) {
                TpsOperations.removeTps(coll);
                tps.getNumber().set(0., 0.);
            } else {
                Iterator<Tps> itr = coll.iterator();
                if (tps.getDepth() == var_index) {
                    while (itr.hasNext()) {
                        Tps tps_inner = itr.next();
                        int ord = tps_inner.getOrder();
                        if (ord > 0) {
                            if (tps_inner.isNumber()) {
                                if (tps_inner.getNumber().abs() > 0.) {
                                    tps_inner.getNumber().mult(ord);
                                    tps_inner.setOrder(ord - 1);
                                    tps.add(tps_inner);
                                } else {
                                    TpsOperations.removeTps(tps_inner);
                                }
                            } else {
                                TpsOperations.mult(tps_inner, ord);
                                tps_inner.setOrder(ord - 1);
                                tps.add(tps_inner);
                            }
                        }
                    }
                } else {
                    while (itr.hasNext()) {
                        Tps tps_inner = itr.next();
                        if (!tps_inner.isNumber()) {
                            TpsOperations.diff(tps_inner, var_index);
                            if (!tps_inner.isNumber() || tps_inner.getNumber().abs() > 0.) {
                                tps.add(tps_inner);
                            } else {
                                TpsOperations.removeTps(tps_inner);
                            }
                        } else {
                            TpsOperations.removeTps(tps_inner);
                        }
                    }
                }
            }
        }
    }

    /**
	 *  This method calculates the integral of the Tps over one variable in place.
	 *
	 *@param  tps                         The Tps object that will be changed.
	 *@param  var_index                   The variable index
	 *@exception  TpsOperationsException  The Exception
	 */
    public static void integral(Tps tps, int var_index) throws TpsOperationsException {
        if (tps.isNumber()) {
            if (tps.getParent() != null) {
                System.err.println("===== TpsOperations class. integral(Tps tps, int var_index)==========");
                System.err.println("This method can not handle Tps number that have a parent!");
                System.err.println("Tps tps=" + tps.toString());
                throw new TpsOperationsException();
            }
            if (tps.getNumber().abs() == 0.) {
                return;
            }
            var_index = var_index - tps.getDepth();
            TpsComplex num = TpsOperations.getTpsComplex();
            num.set(tps.getNumber());
            Tps parent = tps;
            Tps child = null;
            for (int i = 0; i <= var_index; i++) {
                child = TpsOperations.getTps();
                if (i == var_index) {
                    child.setOrder(1);
                    child.getNumber().set(num);
                } else {
                    child.setOrder(0);
                }
                parent.add(child);
                parent = child;
            }
            TpsOperations.removeTpsComplex(num);
        } else {
            int depth = tps.getDepth();
            Collection<Tps> coll = tps.removeAllChildren();
            Iterator<Tps> itr = coll.iterator();
            while (itr.hasNext()) {
                Tps tps_inner = itr.next();
                if (depth == var_index) {
                    int ord = tps_inner.getOrder();
                    tps_inner.setOrder(ord + 1);
                    TpsOperations.mult(tps_inner, 1. / (ord + 1));
                } else {
                    TpsOperations.integral(tps_inner, var_index);
                }
                tps.add(tps_inner);
            }
        }
    }

    /**
	*  This method removes all variables with depth higher than the parameter.
	*
	*@param  tps                         The Tps object that will be changed.
	*@param  var_index_max               The maximal index of the variable
	*/
    public static void removeHighIndexes(Tps tps, int var_index_max) {
        int depth = tps.getDepth();
        if (depth > var_index_max) {
            if (tps.isNumber() && tps.getOrder() == 0) {
                return;
            }
            Collection<Tps> coll = tps.removeAllChildren();
            TpsOperations.removeTps(coll);
            return;
        }
        if (tps.isNumber()) {
            return;
        }
        Collection<Tps> coll = tps.getTpsCollection();
        Iterator<Tps> itr = coll.iterator();
        while (itr.hasNext()) {
            Tps tps_inner = itr.next();
            if (!tps_inner.isNumber()) {
                TpsOperations.removeHighIndexes(tps_inner, var_index_max);
            }
        }
        if (depth == 0) {
            TpsOperations.removeZeros(tps);
        }
    }

    /**
	 *  Calculates the difference between two Tps's.
	 *
	 *@param  tps1                        The Tps object that will be changed.
	 *@param  tps2                        The Tps object will be subtracted
	 *@exception  TpsOperationsException  The Exception
	 */
    public static void sub(Tps tps1, Tps tps2) throws TpsOperationsException {
        if (tps1.isNumber() || tps2.isNumber()) {
            if (tps1.isNumber()) {
                if (tps2.isNumber()) {
                    tps1.getNumber().sub(tps2.getNumber());
                } else {
                    Tps tps = TpsOperations.getTps();
                    tps.setOrder(0);
                    tps.getNumber().set(tps1.getNumber());
                    tps1.add(tps);
                    TpsOperations.sub(tps1, tps2);
                }
            } else {
                if (tps2.isNumber()) {
                    Tps tps = TpsOperations.getTps();
                    tps.setOrder(0);
                    tps.getNumber().set(tps2.getNumber());
                    tps2.add(tps);
                    TpsOperations.sub(tps1, tps2);
                } else {
                    System.err.println("===== TpsOperations.sub(tps1,tps2) =====");
                    System.err.println("Here both Tps instances are not numbers!");
                    System.err.println("That is nonsense!");
                    System.err.println("Stop!");
                    throw new TpsOperationsException();
                }
            }
        } else {
            ArrayList<Integer> orders1 = tps1.getOrders();
            ArrayList<Integer> orders2 = tps2.getOrders();
            Iterator<Integer> itr2 = orders2.iterator();
            while (itr2.hasNext()) {
                Integer o = itr2.next();
                if (orders1.contains(o)) {
                    Tps sub_tps1 = tps1.getTps(o);
                    Tps sub_tps2 = tps2.getTps(o);
                    TpsOperations.sub(sub_tps1, sub_tps2);
                } else {
                    Tps sub_tps = TpsOperations.copy(tps2.getTps(o));
                    TpsOperations.mult(sub_tps, -1.0);
                    tps1.add(sub_tps);
                }
            }
        }
    }

    /**
	 *  Calculates the difference between a Tps object and a number.
	 *
	 *@param  tps  The Tps object that will be changed.
	 *@param  re   The real part of the number
	 *@param  im   The imaginary part of the number
	 */
    public static void sub(Tps tps, double re, double im) {
        Tps tps1 = TpsOperations.getTps();
        tps1.setOrder(0);
        tps1.getNumber().set(-re, -im);
        TpsOperations.add(tps, tps1);
        TpsOperations.removeTps(tps1);
    }

    /**
	 *  Calculates the difference between a Tps object and a number.
	 *
	 *@param  tps  The Tps object that will be changed.
	 *@param  d    The double number
	 */
    public static void sub(Tps tps, double d) {
        Tps tps1 = TpsOperations.getTps();
        tps1.setOrder(0);
        tps1.getNumber().set(-d);
        TpsOperations.add(tps, tps1);
        TpsOperations.removeTps(tps1);
    }

    /**
	 *  Calculates the difference between a Tps object and a number.
	 *
	 *@param  tps  The Tps object that will be changed.
	 *@param  i    The integer number
	 */
    public static void sub(Tps tps, int i) {
        Tps tps1 = TpsOperations.getTps();
        tps1.setOrder(0);
        tps1.getNumber().set(-i);
        TpsOperations.add(tps, tps1);
        TpsOperations.removeTps(tps1);
    }

    /**
	 *  Calculates (tps)^alpha of Tps object. The alpha variable can be anything.
	 *
	 *@param  tps  The Tps object as a source of exp(tps).
	 *@param  alpha The arbitrary value.
	 *@param  ord_max The Tps will be truncated to this order.
	 *@return           The (tps)^alpha of the initial Tps object
	 */
    public static Tps pow(Tps tps, double alpha, int ord_max) throws TpsOperationsException {
        double alpha_floor = Math.floor(alpha);
        Tps tps_res_floor = TpsOperations.pow(tps, (int) alpha_floor, ord_max);
        if (alpha_floor == alpha) {
            return tps_res_floor;
        }
        double pow = alpha - alpha_floor;
        Tps tps_init = TpsOperations.copy(tps);
        TpsComplex v0 = TpsOperations.getZeroOrderNumber(tps_init);
        if (v0.getRe() == 0. || v0.getRe() < 0. || v0.getIm() != 0.) {
            System.err.println("===== TpsOperations.pow(Tps tps, double alpha, int ord_max) =====");
            System.err.println("This could be done if tps has a real non-zero zero order.");
            System.err.println("alpha =" + alpha);
            System.err.println("ord_max =" + ord_max);
            System.err.println("tps =" + tps.toString());
            System.err.println("Stop!");
            throw new TpsOperationsException();
        }
        TpsOperations.removeZeroOrder(tps_init);
        TpsOperations.mult(tps_init, 1.0 / v0.getRe());
        double coeff = Math.pow(v0.getRe(), pow);
        Tps tps_res = TpsOperations.getTps();
        Tps tps0 = TpsOperations.getTps();
        TpsOperations.add(tps0, 1.0);
        for (int i = 0; i <= ord_max; i++) {
            TpsOperations.add(tps_res, tps0);
            if (i != ord_max) {
                TpsOperations.mult(tps0, tps_init);
                TpsOperations.mult(tps0, (pow - i) / (i + 1));
                TpsOperations.truncate(tps0, ord_max);
            }
        }
        TpsOperations.mult(tps_res, coeff);
        TpsOperations.mult(tps_res, tps_res_floor);
        TpsOperations.truncate(tps_res, ord_max);
        TpsOperations.removeTps(tps_res_floor);
        TpsOperations.removeTps(tps_init);
        TpsOperations.removeTps(tps0);
        return tps_res;
    }

    /**
	 *  Calculates (tps)^alpha of Tps object. The alpha variable can be anything.
	 *
	 *@param  tps  The Tps object as a source of exp(tps).
	 *@param  alpha The arbitrary integer value.
	 *@param  ord_max The final Tps will be truncated to this order.
	 *@return           The (tps)^alpha of the initial Tps object
	 */
    public static Tps pow(Tps tps, int alpha, int ord_max) throws TpsOperationsException {
        if (alpha >= 0) {
            Tps tps_res = TpsOperations.getTps();
            TpsOperations.add(tps_res, 1.0);
            for (int i = 0; i < alpha; i++) {
                TpsOperations.mult(tps_res, tps);
                TpsOperations.truncate(tps_res, ord_max);
            }
            return tps_res;
        }
        Tps tps_tmp = TpsOperations.pow(tps, -alpha, ord_max);
        Tps tps_res = TpsOperations.oneOver(tps, ord_max);
        return tps_res;
    }

    /**
	 *  Calculates 1/tps of Tps object. This could be done
	 *  if tps has non-zero zero order.
	 *
	 *@param  tps  The Tps object as a source of 1/tps.
	 *@param  ord_max The final Tps will be truncated to this order.
	 *@return           The 1/tps of the initial Tps object
	 */
    public static Tps oneOver(Tps tps, int ord_max) throws TpsOperationsException {
        TpsComplex v0 = TpsOperations.getZeroOrderNumber(tps);
        if (v0.getRe() == 0. || v0.getIm() != 0.) {
            System.err.println("===== TpsOperations.oneOver(Tps tps, int ord_max) =====");
            System.err.println("This could be done if tps has a real non-zero zero order.");
            System.err.println("tps =" + tps.toString());
            System.err.println("Stop!");
            throw new TpsOperationsException();
        }
        Tps tps_init = TpsOperations.copy(tps);
        TpsOperations.removeZeroOrder(tps_init);
        TpsOperations.mult(tps_init, -1.0 / v0.getRe());
        Tps tps0 = TpsOperations.getTps();
        TpsOperations.add(tps0, 1.0);
        Tps tps_res = TpsOperations.getTps();
        for (int i = 0; i <= ord_max; i++) {
            TpsOperations.add(tps_res, tps0);
            if (i != ord_max) {
                TpsOperations.mult(tps0, tps_init);
                TpsOperations.truncate(tps0, ord_max);
            }
        }
        TpsOperations.mult(tps_res, 1. / v0.getRe());
        TpsOperations.removeTps(tps0);
        TpsOperations.removeTps(tps_init);
        TpsOperations.removeTpsComplex(v0);
        return tps_res;
    }

    /**
	 *  Calculates the exp function of Tps object.
	 *
	 *@param  tps  The Tps object as a source of exp(tps).
	 *@param  ord_max The Tps will be truncated to this order.
	 *@return           The exp(tps) of the initial Tps object
	 */
    public static Tps exp(Tps tps, int ord_max) {
        Tps tps_init = TpsOperations.copy(tps);
        TpsComplex v0 = TpsOperations.getZeroOrderNumber(tps_init);
        double exp_v0_re = Math.exp(v0.getRe());
        double sin_v0_im = exp_v0_re * Math.sin(v0.getIm());
        double cos_v0_im = exp_v0_re * Math.cos(v0.getIm());
        TpsOperations.removeZeroOrder(tps_init);
        Tps tps0 = TpsOperations.copy(tps_init);
        Tps tps_res = TpsOperations.getTps();
        for (int i = 0, n = ord_max; i <= n; i++) {
            TpsOperations.add(tps_res, tps0);
            if (i != n) {
                TpsOperations.mult(tps0, tps_init);
                TpsOperations.truncate(tps0, ord_max);
                TpsOperations.mult(tps0, 1.0 / (i + 2));
            }
        }
        TpsOperations.mult(tps_res, cos_v0_im, sin_v0_im);
        TpsOperations.removeTps(tps0);
        TpsOperations.removeTps(tps_init);
        TpsOperations.removeTpsComplex(v0);
        TpsOperations.truncate(tps_res, ord_max);
        return tps_res;
    }

    /**
	 *  Calculates the cos function of Tps object.
	 *
	 *@param  tps  The Tps object as a source of cos(tps).
	 *@param  ord_max The Tps will be truncated to this order.
	 *@return           The cos(tps) of the initial Tps object
	 */
    public static Tps cos(Tps tps, int ord_max) {
        Tps tps_init = TpsOperations.copy(tps);
        TpsComplex v0 = TpsOperations.getZeroOrderNumber(tps_init);
        TpsComplex v_sin = TpsOperations.getTpsComplex();
        TpsComplex v_cos = TpsOperations.getTpsComplex();
        double v_sin_re = Math.sin(v0.getRe()) * Math.cosh(v0.getIm());
        double v_sin_im = Math.cos(v0.getRe()) * Math.sinh(v0.getIm());
        v_sin.set(v_sin_re, v_sin_im);
        double v_cos_re = Math.cos(v0.getRe()) * Math.cosh(v0.getIm());
        double v_cos_im = -Math.sin(v0.getRe()) * Math.sinh(v0.getIm());
        v_cos.set(v_cos_re, v_cos_im);
        TpsOperations.removeZeroOrder(tps_init);
        Tps tps_sin = TpsOperations.sin_set(tps_init, ord_max);
        Tps tps_cos = TpsOperations.cos_set(tps_init, ord_max);
        TpsOperations.mult(tps_cos, v_cos.getRe(), v_cos.getIm());
        TpsOperations.mult(tps_sin, v_sin.getRe(), v_sin.getIm());
        TpsOperations.sub(tps_cos, tps_sin);
        TpsOperations.removeTps(tps_init);
        TpsOperations.removeTps(tps_sin);
        TpsOperations.removeTpsComplex(v_sin);
        TpsOperations.removeTpsComplex(v_cos);
        TpsOperations.truncate(tps_cos, ord_max);
        return tps_cos;
    }

    /**
	 *  Calculates the sin function of Tps object.
	 *
	 *@param  tps  The Tps object as a source of sin(tps).
	 *@param  ord_max The Tps will be truncated to this order.
	 *@return           The sin(tps) of the initial Tps object
	 */
    public static Tps sin(Tps tps, int ord_max) {
        Tps tps_init = TpsOperations.copy(tps);
        TpsComplex v0 = TpsOperations.getZeroOrderNumber(tps_init);
        TpsComplex v_sin = TpsOperations.getTpsComplex();
        TpsComplex v_cos = TpsOperations.getTpsComplex();
        double v_sin_re = Math.sin(v0.getRe()) * Math.cosh(v0.getIm());
        double v_sin_im = Math.cos(v0.getRe()) * Math.sinh(v0.getIm());
        v_sin.set(v_sin_re, v_sin_im);
        double v_cos_re = Math.cos(v0.getRe()) * Math.cosh(v0.getIm());
        double v_cos_im = -Math.sin(v0.getRe()) * Math.sinh(v0.getIm());
        v_cos.set(v_cos_re, v_cos_im);
        TpsOperations.removeZeroOrder(tps_init);
        Tps tps_sin = TpsOperations.sin_set(tps_init, ord_max);
        Tps tps_cos = TpsOperations.cos_set(tps_init, ord_max);
        TpsOperations.mult(tps_cos, v_sin.getRe(), v_sin.getIm());
        TpsOperations.mult(tps_sin, v_cos.getRe(), v_cos.getIm());
        TpsOperations.add(tps_cos, tps_sin);
        TpsOperations.removeTps(tps_init);
        TpsOperations.removeTps(tps_sin);
        TpsOperations.removeTpsComplex(v_sin);
        TpsOperations.removeTpsComplex(v_cos);
        TpsOperations.truncate(tps_cos, ord_max);
        return tps_cos;
    }

    /**
	 *  Calculates the sin set as sum((-1)^i*(tps^(2i+1))/(2i+1)!, i=0,n/2).
	 *
	 *@param  tps  The input Tps object.
	 *@param  ord_max The Tps will be truncated to this order.
	 */
    private static Tps sin_set(Tps tps, int ord_max) {
        Tps tps0 = TpsOperations.copy(tps);
        Tps tps2 = TpsOperations.copy(tps);
        TpsOperations.mult(tps2, tps);
        Tps tps_res = TpsOperations.getTps();
        for (int i = 0, n = ord_max / 2; i <= n; i++) {
            TpsOperations.add(tps_res, tps0);
            if (i != n) {
                TpsOperations.mult(tps0, tps2);
                TpsOperations.truncate(tps0, ord_max);
                TpsOperations.mult(tps0, -1.0 / ((2.0 * i + 2.0) * (2.0 * i + 3.0)));
            }
        }
        TpsOperations.removeTps(tps0);
        TpsOperations.removeTps(tps2);
        return tps_res;
    }

    /**
	 *  Calculates the cos set as sum((-1)^i*(tps^(2i))/(2i)!, i=0,n/2).
	 *
	 *@param  tps  The input Tps object.
	 *@param  ord_max The Tps will be truncated to this order.
	 */
    private static Tps cos_set(Tps tps, int ord_max) {
        Tps tps0 = TpsOperations.getTps();
        TpsOperations.add(tps0, 1.0);
        Tps tps2 = TpsOperations.copy(tps);
        TpsOperations.mult(tps2, tps);
        Tps tps_res = TpsOperations.getTps();
        for (int i = 0, n = ord_max / 2; i <= n; i++) {
            TpsOperations.add(tps_res, tps0);
            if (i != n) {
                TpsOperations.mult(tps0, tps2);
                TpsOperations.mult(tps0, -1.0 / ((2.0 * i + 1) * (2.0 * i + 2.0)));
            }
        }
        TpsOperations.removeTps(tps0);
        TpsOperations.removeTps(tps2);
        return tps_res;
    }

    /**
	 *  Copies the Tps object.
	 *
	 *@param  tps_init  The Tps object to copy
	 *@return           The copy of the initial Tps object
	 */
    public static Tps copy(Tps tps_init) {
        Tps tps = TpsOperations.getTps();
        tps.setDepth(tps_init.getDepth());
        tps.setOrder(tps_init.getOrder());
        if (tps_init.isNumber()) {
            tps.getNumber().set(tps_init.getNumber());
        } else {
            ArrayList<Integer> orders = tps_init.getOrders();
            Iterator<Integer> itr = orders.iterator();
            while (itr.hasNext()) {
                Integer o = itr.next();
                Tps tps_inner = TpsOperations.copy(tps_init.getTps(o));
                tps.add(tps_inner);
            }
        }
        return tps;
    }

    /**
	 *  Truncates the power series.
	 *
	 *@param  tps        The Tps object that will be truncated
	 *@param  order_max  The maximal order parameter
	 */
    public static void truncate(Tps tps, int order_max) {
        if (tps.isNumber()) {
            if (tps.getOrder() > order_max) {
                if (tps.getParent() != null) {
                    TpsOperations.removeTps(tps.getParent().removeChild(tps));
                } else {
                    tps.setOrder(0);
                    tps.getNumber().set(0., 0.);
                }
            }
        } else {
            int ord = tps.getOrder();
            if (ord > order_max) {
                if (tps.getParent() != null) {
                    TpsOperations.removeTps(tps.getParent().removeChild(tps));
                } else {
                    Collection<Tps> coll_toRemove = tps.removeAllChildren();
                    TpsOperations.removeTps(coll_toRemove);
                    tps.setOrder(0);
                    tps.getNumber().set(0., 0.);
                }
            } else {
                ArrayList<Tps> coll = new ArrayList<Tps>(tps.getTpsCollection());
                Iterator<Tps> itr = coll.iterator();
                while (itr.hasNext()) {
                    Tps tps_inner = itr.next();
                    int ord_inner = tps_inner.getOrder();
                    if ((ord + ord_inner) > order_max) {
                        TpsOperations.removeTps(tps.removeChild(tps_inner));
                    } else {
                        TpsOperations.truncate(tps_inner, order_max - ord);
                    }
                }
            }
        }
    }

    /**
	 *  Removes all child numbers with small number values.
	 *
	 *@param  tps  The Tps object to be cleaned
	 *@param  eps  The absolute value threshold
	 *@return      The true if the operation was a success.
	 */
    public static boolean removeZeros(Tps tps, double eps) {
        boolean res = false;
        while (TpsOperations.removeZerosInner(tps, eps)) {
            res = true;
        }
        return res;
    }

    /**
	*  Removes all child numbers with small number values.
	*
	*@param  tps  The Tps object to be cleaned
	*@param  eps  The absolute value threshold
	*@return      The true if the operation was a success.
	*/
    public static boolean removeZerosInner(Tps tps, double eps) {
        if (tps.isNumber()) {
            double val = tps.getNumber().abs();
            if (val <= eps) {
                if (tps.getParent() != null) {
                    TpsOperations.removeTps(tps.getParent().removeChild(tps));
                } else {
                    tps.setOrder(0);
                    tps.getNumber().set(0., 0.);
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } else {
            boolean res = false;
            ArrayList<Tps> coll = new ArrayList<Tps>(tps.getTpsCollection());
            Iterator<Tps> itr = coll.iterator();
            while (itr.hasNext()) {
                Tps tps_inner = itr.next();
                if (TpsOperations.removeZerosInner(tps_inner, eps)) {
                    res = true;
                }
            }
            return res;
        }
    }

    /**
	 *  Removes all child numbers with zero values.
	 *
	 *@param  tps  The Tps object to be cleaned
	 *@return      The true if the operation was a success
	 */
    public static boolean removeZeros(Tps tps) {
        return TpsOperations.removeZeros(tps, 0.);
    }

    /**
	 *  Returns the number after substituting the values inside the Tps.
	 *
	 *@param  tps                         The Tps object to be evaluated
	 *@param  vars                        The complex numbers arrays
	 *@return                             The resulting complex number
	 *@exception  TpsOperationsException  The Exception
	 */
    public static TpsComplex calculate(Tps tps, TpsComplex[] vars) throws TpsOperationsException {
        TpsComplex res = new TpsComplex();
        if (!tps.isNumber()) {
            int maxDepth = tps.getMaxDepth();
            if (vars.length >= maxDepth) {
                int depth = tps.getDepth();
                Collection<Tps> coll = tps.getTpsCollection();
                Iterator<Tps> itr = coll.iterator();
                TpsComplex res_tmp = TpsOperations.getTpsComplex();
                while (itr.hasNext()) {
                    Tps tps_inner = itr.next();
                    int ord = tps_inner.getOrder();
                    res_tmp.set(vars[depth]);
                    res_tmp.pow(ord);
                    res_tmp.mult(TpsOperations.calculate(tps_inner, vars));
                    res.add(res_tmp);
                }
                TpsOperations.removeTpsComplex(res_tmp);
            } else {
                System.err.println("===== TpsOperations.calculate(Tps tps, TpsComplex[] vars) =====");
                System.err.println("Can not calculate value of Tps!");
                System.err.println("The number of variables is more than you gave!");
                System.err.println("Tps =" + tps);
                System.err.println("maxDepth(Tps) =" + maxDepth);
                System.err.println("vars.length =" + vars.length);
                System.err.println("Stop!");
                throw new TpsOperationsException();
            }
        } else {
            res.set(tps.getNumber());
        }
        return res;
    }

    /**
	 *  Returns the unity Tps. It equals x^1*(1.0)
	 *
	 *@param  depth  The depth index
	 *@return        The unity Tps
	 */
    public static Tps getUnitaryTps(int depth) {
        Tps tps = TpsOperations.getTps();
        tps.clear();
        tps.setDepth(0);
        Tps tps_parent = tps;
        Tps tps_child = null;
        for (int i = 0, n = depth; i <= n; i++) {
            tps_child = TpsOperations.getTps();
            tps_child.clear();
            if (i != n) {
                tps_child.setOrder(0);
            } else {
                tps_child.setOrder(1);
                tps_child.getNumber().set(1.0, 0.);
            }
            tps_parent.add(tps_child);
            tps_parent = tps_child;
        }
        return tps;
    }

    /**
	 *  Returns the norm of this Tps. The norm is the maximal
	 *  module of the coefficient in the Tps. The zero order is not included
	 *  into considerations.
	 *
	 *@param  tps  The Tps object.
	 *@return      The maximal module of the coefficient in this Tps.
	 */
    public static double getNorm(Tps tps) {
        double norm = 0.;
        if (tps.isNumber()) {
            if (tps.getOrder() != 0) {
                return tps.getNumber().abs();
            }
            return norm;
        }
        Collection<Tps> coll = tps.getTpsCollection();
        Iterator<Tps> itr = coll.iterator();
        while (itr.hasNext()) {
            Tps tps_inner = itr.next();
            int commulative_order = tps_inner.getOrder();
            norm = TpsOperations.getNorm(norm, commulative_order, tps_inner);
        }
        return norm;
    }

    /**
	 *  Returns the norm of this Tps. The norm is the maximal
	 *  module of the coefficient in the Tps. The zero order is not included
	 *  into considerations.
	 *
	 *@param  tps  The Tps object.
	 *@param  norm_in The existing norm.
	 *@param 	commulative_order The sum of all previous orders from all variables
	 *@return      The maximal module of the coefficient in this Tps.
	 */
    private static double getNorm(double norm_in, int commulative_order, Tps tps) {
        double norm = norm_in;
        if (tps.isNumber()) {
            if ((tps.getOrder() + commulative_order) != 0) {
                double norm_tmp = tps.getNumber().abs();
                if (norm_tmp > norm) {
                    norm = norm_tmp;
                }
            }
            return norm;
        }
        Collection<Tps> coll = tps.getTpsCollection();
        Iterator<Tps> itr = coll.iterator();
        while (itr.hasNext()) {
            Tps tps_inner = itr.next();
            int ord = tps_inner.getOrder();
            norm = TpsOperations.getNorm(norm, commulative_order + ord, tps_inner);
        }
        return norm;
    }

    /**
	 *  Returns zero order value in Tps.
	 *
	 *@param  tps  The Tps object.
	 *@return      The zero order TpsComplex number from tps.
	 */
    public static TpsComplex getZeroOrderNumber(Tps tps) {
        TpsComplex cmplx = TpsOperations.getTpsComplex();
        cmplx.set(0., 0.);
        if (tps.isNumber() && (tps.getOrder() == 0)) {
            cmplx.set(tps.getNumber());
        } else {
            ArrayList<Integer> ords = tps.getOrders();
            if (ords.contains(intArr[0])) {
                Tps tps0 = tps.getTps(intArr[0]);
                TpsComplex t_cmplx = TpsOperations.getZeroOrderNumber(tps0);
                cmplx.set(t_cmplx);
                TpsOperations.removeTpsComplex(t_cmplx);
            } else {
                cmplx.set(0., 0.);
            }
        }
        return cmplx;
    }

    /**
	 *  Returns 1st order values in Tps as an array of TpsComplex.
	 *  The start index is a parameter.
	 *
	 *@param  tps      The Tps object.
	 *@param  cmplArr  The array of TpsComplex.
	 *@param  ind      The running index of the array element.
	 *
	 */
    private static void getFirstOrderArray(Tps tps, TpsComplex[] cmplArr, int ind) {
        int n = cmplArr.length;
        if (ind == n) {
            return;
        }
        if (tps == null) {
            cmplArr[ind].set(0.);
            TpsOperations.getFirstOrderArray(null, cmplArr, ind + 1);
            return;
        }
        if (tps.isNumber() && tps.getOrder() == 1) {
            cmplArr[ind].set(tps.getNumber());
            TpsOperations.getFirstOrderArray(null, cmplArr, ind + 1);
            return;
        }
        ArrayList<Integer> ords = tps.getOrders();
        if (ords.contains(intArr[1])) {
            Tps tps1 = tps.getTps(intArr[1]);
            if (tps1.isNumber()) {
                cmplArr[ind].set(tps1.getNumber());
            } else {
                TpsComplex cmplx = TpsOperations.getZeroOrderNumber(tps1);
                cmplArr[ind].set(cmplx);
                TpsOperations.removeTpsComplex(cmplx);
            }
        } else {
            cmplArr[ind].set(0., 0.);
        }
        if (ords.contains(intArr[0])) {
            Tps tps0 = tps.getTps(intArr[0]);
            TpsOperations.getFirstOrderArray(tps0, cmplArr, ind + 1);
        } else {
            TpsOperations.getFirstOrderArray(null, cmplArr, ind + 1);
        }
    }

    /**
	 *  Returns 1st order values in Tps as an array of TpsComplex.
	 *
	 *@param  tps      The Tps object.
	 *@param  cmplArr  The array of TpsComplex.
	 */
    public static void getFirstOrderArray(Tps tps, TpsComplex[] cmplArr) {
        TpsOperations.getFirstOrderArray(tps, cmplArr, 0);
    }

    /**
	 *  Removes all zero order values in Tps and clean up Tps object.
	 *  This Tps object will be changed in place.
	 *
	 *@param  tps      The Tps object.
	 *
	 */
    public static void removeZeroOrder(Tps tps) {
        TpsOperations.removeZeroOrderInner(tps);
        TpsOperations.removeZeros(tps);
    }

    /**
	 *  Removes all zero order values in Tps.
	 *  This Tps object will be changed in place.
	 *
	 *@param  tps      The Tps object.
	 *
	 */
    private static void removeZeroOrderInner(Tps tps) {
        if (tps.isNumber()) {
            if (tps.getOrder() == 0) {
                tps.getNumber().set(0.);
            }
        } else {
            ArrayList<Integer> ords = tps.getOrders();
            if (ords.contains(intArr[0])) {
                Tps tps_inner = tps.getTps(intArr[0]);
                TpsOperations.removeZeroOrderInner(tps_inner);
            }
        }
    }

    /**
	 *  Removes all first order values in Tps and clean up Tps object.
	 *  This Tps object will be changed in place.
	 *
	 *@param  tps      The Tps object.
	 *
	 */
    public static void removeFirstOrders(Tps tps) {
        TpsOperations.removeFirstOrdersInner(tps);
        TpsOperations.removeZeros(tps);
    }

    /**
	 *  Removes all first order values in Tps.
	 *  This Tps object will be changed in place.
	 *
	 *@param  tps      The Tps object.
	 *
	 */
    private static void removeFirstOrdersInner(Tps tps) {
        if (tps.isNumber()) {
            if (tps.getOrder() == 1) {
                tps.getNumber().set(0.);
            }
        } else {
            ArrayList<Integer> ords = tps.getOrders();
            if (ords.contains(intArr[0])) {
                Tps tps_inner = tps.getTps(intArr[0]);
                TpsOperations.removeFirstOrdersInner(tps_inner);
            }
            if (ords.contains(intArr[1])) {
                Tps tps_inner = tps.getTps(intArr[1]);
                if (tps_inner.isNumber()) {
                    tps_inner.getNumber().set(0.);
                } else {
                    TpsOperations.removeZeroOrderInner(tps_inner);
                }
            }
        }
    }

    /**
	 *  Returns the new Tps instance.
	 *
	 *@return    The new Tps instance
	 */
    public static Tps getTps() {
        return (new Tps());
    }

    /**
	 *  Removes an existing Tps from a pool.
	 *
	 *@param  tps  The Tps instance
	 */
    public static void removeTps(Tps tps) {
    }

    /**
	 *  Removes a collection of existing Tps' from a pool.
	 *
	 *@param  tpsColl  The collection of Tps'
	 */
    public static void removeTps(Collection<Tps> tpsColl) {
        Iterator<Tps> itr = tpsColl.iterator();
        while (itr.hasNext()) {
            TpsOperations.removeTps(itr.next());
        }
    }

    /**
	 *  Returns the new complex number
	 *
	 *@return    The complex number
	 */
    public static TpsComplex getTpsComplex() {
        return (new TpsComplex());
    }

    /**
	 *  Removes a complex number from a pool
	 *
	 *@param  c  The complex number
	 */
    public static void removeTpsComplex(TpsComplex c) {
    }
}
