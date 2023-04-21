package com.howbuy.wireless.entity.protobuf;

public final class UpDownProtos {

    private UpDownProtos() {
    }

    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    }

    public static final class UpDown extends com.google.protobuf.GeneratedMessage {

        private UpDown() {
            initFields();
        }

        private UpDown(boolean noInit) {
        }

        private static final UpDown defaultInstance;

        public static UpDown getDefaultInstance() {
            return defaultInstance;
        }

        public UpDown getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.howbuy.wireless.entity.protobuf.UpDownProtos.internal_static_UpDown_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.howbuy.wireless.entity.protobuf.UpDownProtos.internal_static_UpDown_fieldAccessorTable;
        }

        public static final int COMMON_FIELD_NUMBER = 1;

        private boolean hasCommon;

        private com.howbuy.wireless.entity.protobuf.CommonProtos.Common common_;

        public boolean hasCommon() {
            return hasCommon;
        }

        public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() {
            return common_;
        }

        public static final int UPTIMES_FIELD_NUMBER = 2;

        private boolean hasUpTimes;

        private int upTimes_ = 0;

        public boolean hasUpTimes() {
            return hasUpTimes;
        }

        public int getUpTimes() {
            return upTimes_;
        }

        public static final int DOWNTIMES_FIELD_NUMBER = 3;

        private boolean hasDownTimes;

        private int downTimes_ = 0;

        public boolean hasDownTimes() {
            return hasDownTimes;
        }

        public int getDownTimes() {
            return downTimes_;
        }

        private void initFields() {
            common_ = com.howbuy.wireless.entity.protobuf.CommonProtos.Common.getDefaultInstance();
        }

        public final boolean isInitialized() {
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            getSerializedSize();
            if (hasCommon()) {
                output.writeMessage(1, getCommon());
            }
            if (hasUpTimes()) {
                output.writeInt32(2, getUpTimes());
            }
            if (hasDownTimes()) {
                output.writeInt32(3, getDownTimes());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;
            size = 0;
            if (hasCommon()) {
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getCommon());
            }
            if (hasUpTimes()) {
                size += com.google.protobuf.CodedOutputStream.computeInt32Size(2, getUpTimes());
            }
            if (hasDownTimes()) {
                size += com.google.protobuf.CodedOutputStream.computeInt32Size(3, getDownTimes());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown();
                return builder;
            }

            protected com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown.getDescriptor();
            }

            public com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown getDefaultInstanceForType() {
                return com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown) {
                    return mergeFrom((com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown other) {
                if (other == com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown.getDefaultInstance()) return this;
                if (other.hasCommon()) {
                    mergeCommon(other.getCommon());
                }
                if (other.hasUpTimes()) {
                    setUpTimes(other.getUpTimes());
                }
                if (other.hasDownTimes()) {
                    setDownTimes(other.getDownTimes());
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder(this.getUnknownFields());
                while (true) {
                    int tag = input.readTag();
                    switch(tag) {
                        case 0:
                            this.setUnknownFields(unknownFields.build());
                            return this;
                        default:
                            {
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    this.setUnknownFields(unknownFields.build());
                                    return this;
                                }
                                break;
                            }
                        case 10:
                            {
                                com.howbuy.wireless.entity.protobuf.CommonProtos.Common.Builder subBuilder = com.howbuy.wireless.entity.protobuf.CommonProtos.Common.newBuilder();
                                if (hasCommon()) {
                                    subBuilder.mergeFrom(getCommon());
                                }
                                input.readMessage(subBuilder, extensionRegistry);
                                setCommon(subBuilder.buildPartial());
                                break;
                            }
                        case 16:
                            {
                                setUpTimes(input.readInt32());
                                break;
                            }
                        case 24:
                            {
                                setDownTimes(input.readInt32());
                                break;
                            }
                    }
                }
            }

            public boolean hasCommon() {
                return result.hasCommon();
            }

            public com.howbuy.wireless.entity.protobuf.CommonProtos.Common getCommon() {
                return result.getCommon();
            }

            public Builder setCommon(com.howbuy.wireless.entity.protobuf.CommonProtos.Common value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasCommon = true;
                result.common_ = value;
                return this;
            }

            public Builder setCommon(com.howbuy.wireless.entity.protobuf.CommonProtos.Common.Builder builderForValue) {
                result.hasCommon = true;
                result.common_ = builderForValue.build();
                return this;
            }

            public Builder mergeCommon(com.howbuy.wireless.entity.protobuf.CommonProtos.Common value) {
                if (result.hasCommon() && result.common_ != com.howbuy.wireless.entity.protobuf.CommonProtos.Common.getDefaultInstance()) {
                    result.common_ = com.howbuy.wireless.entity.protobuf.CommonProtos.Common.newBuilder(result.common_).mergeFrom(value).buildPartial();
                } else {
                    result.common_ = value;
                }
                result.hasCommon = true;
                return this;
            }

            public Builder clearCommon() {
                result.hasCommon = false;
                result.common_ = com.howbuy.wireless.entity.protobuf.CommonProtos.Common.getDefaultInstance();
                return this;
            }

            public boolean hasUpTimes() {
                return result.hasUpTimes();
            }

            public int getUpTimes() {
                return result.getUpTimes();
            }

            public Builder setUpTimes(int value) {
                result.hasUpTimes = true;
                result.upTimes_ = value;
                return this;
            }

            public Builder clearUpTimes() {
                result.hasUpTimes = false;
                result.upTimes_ = 0;
                return this;
            }

            public boolean hasDownTimes() {
                return result.hasDownTimes();
            }

            public int getDownTimes() {
                return result.getDownTimes();
            }

            public Builder setDownTimes(int value) {
                result.hasDownTimes = true;
                result.downTimes_ = value;
                return this;
            }

            public Builder clearDownTimes() {
                result.hasDownTimes = false;
                result.downTimes_ = 0;
                return this;
            }
        }

        static {
            defaultInstance = new UpDown(true);
            com.howbuy.wireless.entity.protobuf.UpDownProtos.internalForceInit();
            defaultInstance.initFields();
        }
    }

    private static com.google.protobuf.Descriptors.Descriptor internal_static_UpDown_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_UpDown_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

    static {
        java.lang.String[] descriptorData = { "\n\014UpDown.proto\032\014common.proto\"L\n\006UpDown\022\036" + "\n\006common\030\001 \001(\0132\016.common.Common\022\017\n\007upTime" + "s\030\002 \001(\005\022\021\n\tdownTimes\030\003 \001(\005B3\n#com.howbuy" + ".wireless.entity.protobufB\014UpDownProtos" };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {

            public com.google.protobuf.ExtensionRegistry assignDescriptors(com.google.protobuf.Descriptors.FileDescriptor root) {
                descriptor = root;
                internal_static_UpDown_descriptor = getDescriptor().getMessageTypes().get(0);
                internal_static_UpDown_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_UpDown_descriptor, new java.lang.String[] { "Common", "UpTimes", "DownTimes" }, com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown.class, com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown.Builder.class);
                return null;
            }
        };
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] { com.howbuy.wireless.entity.protobuf.CommonProtos.getDescriptor() }, assigner);
    }

    public static void internalForceInit() {
    }
}
