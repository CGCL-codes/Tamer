package org.t2framework.cloud.amazon.ec2;

public class EC2BasicInstanceTypes {

    public static final EC2InstanceType SMALL = new EC2InstanceType() {

        @Override
        public String getType() {
            return "m1.small";
        }
    };

    public static final EC2InstanceType HIGHCPU_MEDIUM = new EC2InstanceType() {

        @Override
        public String getType() {
            return "c1.medium";
        }
    };

    public static final EC2InstanceType LARGE = new EC2InstanceType() {

        @Override
        public String getType() {
            return "m1.large";
        }
    };

    public static final EC2InstanceType XLARGE = new EC2InstanceType() {

        @Override
        public String getType() {
            return "m1.xlarge";
        }
    };

    public static final EC2InstanceType HIGHMEMORY_XLARGE = new EC2InstanceType() {

        @Override
        public String getType() {
            return "m2.xlarge";
        }
    };

    public static final EC2InstanceType HIGHMEMORY_DOUBLE_XLARGE = new EC2InstanceType() {

        @Override
        public String getType() {
            return "m2.2xlarge";
        }
    };

    public static final EC2InstanceType HIGHMEMORY_QUADRUPLE_XLARGE = new EC2InstanceType() {

        @Override
        public String getType() {
            return "m2.4xlarge";
        }
    };

    public static final EC2InstanceType HIGHCPU_LARGE = new EC2InstanceType() {

        @Override
        public String getType() {
            return "c1.xlarge";
        }
    };
}
