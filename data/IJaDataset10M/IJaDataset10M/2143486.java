package com.otom.bcel.basic;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.otom.bcel.Mapper;
import com.otom.bcel.annotations.MapClassTo;
import com.otom.bcel.annotations.MapTo;

public class InheritanceTest {

    @Test
    public void testSourceToDestination() {
        InheritanceSource source = new InheritanceSource();
        source.setName1("test");
        source.setName2("test1");
        InheritanceDest destination = new InheritanceDest();
        Mapper<InheritanceSource, InheritanceDest> mapper = new Mapper<InheritanceSource, InheritanceDest>();
        mapper.map(source, destination);
        assertEquals("test", destination.getName1());
        assertEquals("test1", destination.getName2());
    }

    @MapClassTo(value = InheritanceDest.class, biDirectional = false, wildcard = false)
    public class InheritanceSource {

        @MapTo(name = "name1")
        private String name1;

        @MapTo(name = "name2")
        private String name2;

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }
    }

    public class BaseDestination {

        @MapTo(name = "name1", forClass = InheritanceSource.class)
        private String name1;

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }
    }

    @MapClassTo(value = InheritanceSource.class, biDirectional = false, wildcard = false)
    public class InheritanceDest extends BaseDestination {

        @MapTo(name = "name2")
        private String name2;

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }
    }
}
