package org.shava.core.runtime;

import org.shava.core.Buffer;
import org.shava.core.impl.BoundedBuffer;
import org.shava.core.impl.MultiOutputBuffer;
import org.shava.core.impl.RefCountBuffer;
import org.shava.element.Element;
import org.shava.element.ElementThread;

public class ShavaRuntime {

    public static void scriptTest1() {
        try {
            Class<Element<?, ?>> ec1 = (Class) Class.forName("org.shava.element.base.ForElement");
            Element<?, ?> e1 = ec1.newInstance();
            ElementThread<?, ?> t1 = new ElementThread();
            t1.setName(ec1.getSimpleName());
            t1.setElement((Element) e1);
            System.out.println("input: " + ElementClassUtils.getInputType(ec1));
            System.out.println("output: " + ElementClassUtils.getOutputType(ec1));
            Class<Element<?, ?>> ec2 = (Class<Element<?, ?>>) Class.forName("org.shava.element.base.ToStringElement");
            Element<?, ?> e2 = ec2.newInstance();
            ElementThread<?, ?> t2 = new ElementThread();
            t2.setName(ec2.getSimpleName());
            t2.setElement((Element) e2);
            System.out.println("input: " + ElementClassUtils.getInputType(ec2));
            System.out.println("output: " + ElementClassUtils.getOutputType(ec2));
            Buffer<?> b1 = new BoundedBuffer(1000);
            RefCountBuffer<?> rb1 = new RefCountBuffer(b1);
            t2.setInputBuffer((Buffer) rb1);
            rb1.acquireReader(t2);
            t1.setOutputBuffer((Buffer) rb1);
            rb1.acquireWriter(t1);
            Class<Element<?, ?>> ec3 = (Class<Element<?, ?>>) Class.forName("org.shava.element.base.StdOutPrinterElement");
            Element<?, ?> e3 = ec3.newInstance();
            ElementThread<?, ?> t3 = new ElementThread();
            t3.setName(ec3.getSimpleName());
            t3.setElement((Element) e3);
            System.out.println("input: " + ElementClassUtils.getInputType(ec3));
            System.out.println("output: " + ElementClassUtils.getOutputType(ec3));
            Buffer b2 = new BoundedBuffer(1000);
            RefCountBuffer rb2 = new RefCountBuffer(b2);
            t3.setInputBuffer(rb2);
            rb2.acquireReader(t3);
            t2.setOutputBuffer(rb2);
            rb2.acquireWriter(t2);
            System.out.println(ElementClassUtils.isSubtype(ElementClassUtils.getOutputType(ec1), ElementClassUtils.getInputType(ec2)));
            System.out.println(ElementClassUtils.isSubtype(ElementClassUtils.getOutputType(ec2), ElementClassUtils.getInputType(ec3)));
            t1.start();
            t2.start();
            t3.start();
        } catch (ClassNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scriptTest2() {
        try {
            Class<Element<?, ?>> ec1 = (Class<Element<?, ?>>) Class.forName("org.shava.element.base.ForElement");
            Element<?, ?> e1 = ec1.newInstance();
            ElementThread<?, ?> t1 = new ElementThread();
            t1.setName(ec1.getSimpleName());
            t1.setElement((Element) e1);
            Class<Element<?, ?>> ec2 = (Class<Element<?, ?>>) Class.forName("org.shava.element.base.ToStringElement");
            Element<?, ?> e2 = ec2.newInstance();
            ElementThread<?, ?> t2 = new ElementThread();
            t2.setName(ec2.getSimpleName());
            t2.setElement((Element) e2);
            Class<Element<?, ?>> ec3 = (Class<Element<?, ?>>) Class.forName("org.shava.element.base.SumaElement");
            Element<?, ?> e3 = ec3.newInstance();
            ElementThread<?, ?> t3 = new ElementThread();
            t3.setName(ec3.getSimpleName());
            t3.setElement((Element) e3);
            Class<Element<?, ?>> ec4 = (Class<Element<?, ?>>) Class.forName("org.shava.element.base.StdOutPrinterElement");
            Element<?, ?> e4 = ec4.newInstance();
            ElementThread<?, ?> t4 = new ElementThread();
            t4.setName(ec4.getSimpleName());
            t4.setElement((Element) e4);
            MultiOutputBuffer b1 = new MultiOutputBuffer();
            RefCountBuffer rcb1 = new RefCountBuffer(b1);
            Buffer b11 = new BoundedBuffer(1000);
            Buffer b12 = new BoundedBuffer(1000);
            b1.addOutput(b11);
            b1.addOutput(b12);
            t1.setOutputBuffer(rcb1);
            rcb1.acquireWriter(t1);
            t2.setInputBuffer(rcb1);
            rcb1.acquireReader(t2);
            t3.setInputBuffer(rcb1);
            rcb1.acquireReader(t3);
            Buffer b2 = new BoundedBuffer(1000);
            RefCountBuffer rcb2 = new RefCountBuffer(b2);
            t2.setOutputBuffer(rcb2);
            rcb2.acquireWriter(t2);
            t3.setOutputBuffer(rcb2);
            rcb2.acquireWriter(t3);
            t4.setInputBuffer(rcb2);
            rcb2.acquireReader(t4);
            t1.start();
            t2.start();
            t3.start();
            t4.start();
        } catch (ClassNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        scriptTest2();
    }
}
