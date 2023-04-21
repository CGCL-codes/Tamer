package com.howbuy.wireless.entity.protobuf;

public final class FundDruationProtos {

    private FundDruationProtos() {
    }

    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    }

    public static final class FundDruationInfo extends com.google.protobuf.GeneratedMessage {

        private FundDruationInfo() {
            initFields();
        }

        private FundDruationInfo(boolean noInit) {
        }

        private static final FundDruationInfo defaultInstance;

        public static FundDruationInfo getDefaultInstance() {
            return defaultInstance;
        }

        public FundDruationInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.howbuy.wireless.entity.protobuf.FundDruationProtos.internal_static_FundDruationInfo_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.howbuy.wireless.entity.protobuf.FundDruationProtos.internal_static_FundDruationInfo_fieldAccessorTable;
        }

        public static final int JZRQ_FIELD_NUMBER = 1;

        private boolean hasJzrq;

        private java.lang.String jzrq_ = "";

        public boolean hasJzrq() {
            return hasJzrq;
        }

        public java.lang.String getJzrq() {
            return jzrq_;
        }

        public static final int JJJZ_FIELD_NUMBER = 2;

        private boolean hasJjjz;

        private java.lang.String jjjz_ = "";

        public boolean hasJjjz() {
            return hasJjjz;
        }

        public java.lang.String getJjjz() {
            return jjjz_;
        }

        private void initFields() {
        }

        public final boolean isInitialized() {
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            getSerializedSize();
            if (hasJzrq()) {
                output.writeString(1, getJzrq());
            }
            if (hasJjjz()) {
                output.writeString(2, getJjjz());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;
            size = 0;
            if (hasJzrq()) {
                size += com.google.protobuf.CodedOutputStream.computeStringSize(1, getJzrq());
            }
            if (hasJjjz()) {
                size += com.google.protobuf.CodedOutputStream.computeStringSize(2, getJjjz());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo();
                return builder;
            }

            protected com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.getDescriptor();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo getDefaultInstanceForType() {
                return com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo) {
                    return mergeFrom((com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo other) {
                if (other == com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.getDefaultInstance()) return this;
                if (other.hasJzrq()) {
                    setJzrq(other.getJzrq());
                }
                if (other.hasJjjz()) {
                    setJjjz(other.getJjjz());
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
                                setJzrq(input.readString());
                                break;
                            }
                        case 18:
                            {
                                setJjjz(input.readString());
                                break;
                            }
                    }
                }
            }

            public boolean hasJzrq() {
                return result.hasJzrq();
            }

            public java.lang.String getJzrq() {
                return result.getJzrq();
            }

            public Builder setJzrq(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasJzrq = true;
                result.jzrq_ = value;
                return this;
            }

            public Builder clearJzrq() {
                result.hasJzrq = false;
                result.jzrq_ = getDefaultInstance().getJzrq();
                return this;
            }

            public boolean hasJjjz() {
                return result.hasJjjz();
            }

            public java.lang.String getJjjz() {
                return result.getJjjz();
            }

            public Builder setJjjz(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasJjjz = true;
                result.jjjz_ = value;
                return this;
            }

            public Builder clearJjjz() {
                result.hasJjjz = false;
                result.jjjz_ = getDefaultInstance().getJjjz();
                return this;
            }
        }

        static {
            defaultInstance = new FundDruationInfo(true);
            com.howbuy.wireless.entity.protobuf.FundDruationProtos.internalForceInit();
            defaultInstance.initFields();
        }
    }

    public static final class FundDruation extends com.google.protobuf.GeneratedMessage {

        private FundDruation() {
            initFields();
        }

        private FundDruation(boolean noInit) {
        }

        private static final FundDruation defaultInstance;

        public static FundDruation getDefaultInstance() {
            return defaultInstance;
        }

        public FundDruation getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.howbuy.wireless.entity.protobuf.FundDruationProtos.internal_static_FundDruation_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.howbuy.wireless.entity.protobuf.FundDruationProtos.internal_static_FundDruation_fieldAccessorTable;
        }

        public static final int FUNDDRUATIONINFO_FIELD_NUMBER = 1;

        private java.util.List<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo> fundDruationInfo_ = java.util.Collections.emptyList();

        public java.util.List<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo> getFundDruationInfoList() {
            return fundDruationInfo_;
        }

        public int getFundDruationInfoCount() {
            return fundDruationInfo_.size();
        }

        public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo getFundDruationInfo(int index) {
            return fundDruationInfo_.get(index);
        }

        public static final int JJDM_FIELD_NUMBER = 2;

        private boolean hasJjdm;

        private java.lang.String jjdm_ = "";

        public boolean hasJjdm() {
            return hasJjdm;
        }

        public java.lang.String getJjdm() {
            return jjdm_;
        }

        private void initFields() {
        }

        public final boolean isInitialized() {
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            getSerializedSize();
            for (com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo element : getFundDruationInfoList()) {
                output.writeMessage(1, element);
            }
            if (hasJjdm()) {
                output.writeString(2, getJjdm());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;
            size = 0;
            for (com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo element : getFundDruationInfoList()) {
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, element);
            }
            if (hasJjdm()) {
                size += com.google.protobuf.CodedOutputStream.computeStringSize(2, getJjdm());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation();
                return builder;
            }

            protected com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.getDescriptor();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation getDefaultInstanceForType() {
                return com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                if (result.fundDruationInfo_ != java.util.Collections.EMPTY_LIST) {
                    result.fundDruationInfo_ = java.util.Collections.unmodifiableList(result.fundDruationInfo_);
                }
                com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation) {
                    return mergeFrom((com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation other) {
                if (other == com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.getDefaultInstance()) return this;
                if (!other.fundDruationInfo_.isEmpty()) {
                    if (result.fundDruationInfo_.isEmpty()) {
                        result.fundDruationInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo>();
                    }
                    result.fundDruationInfo_.addAll(other.fundDruationInfo_);
                }
                if (other.hasJjdm()) {
                    setJjdm(other.getJjdm());
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
                                com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.Builder subBuilder = com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.newBuilder();
                                input.readMessage(subBuilder, extensionRegistry);
                                addFundDruationInfo(subBuilder.buildPartial());
                                break;
                            }
                        case 18:
                            {
                                setJjdm(input.readString());
                                break;
                            }
                    }
                }
            }

            public java.util.List<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo> getFundDruationInfoList() {
                return java.util.Collections.unmodifiableList(result.fundDruationInfo_);
            }

            public int getFundDruationInfoCount() {
                return result.getFundDruationInfoCount();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo getFundDruationInfo(int index) {
                return result.getFundDruationInfo(index);
            }

            public Builder setFundDruationInfo(int index, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.fundDruationInfo_.set(index, value);
                return this;
            }

            public Builder setFundDruationInfo(int index, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.Builder builderForValue) {
                result.fundDruationInfo_.set(index, builderForValue.build());
                return this;
            }

            public Builder addFundDruationInfo(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.fundDruationInfo_.isEmpty()) {
                    result.fundDruationInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo>();
                }
                result.fundDruationInfo_.add(value);
                return this;
            }

            public Builder addFundDruationInfo(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.Builder builderForValue) {
                if (result.fundDruationInfo_.isEmpty()) {
                    result.fundDruationInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo>();
                }
                result.fundDruationInfo_.add(builderForValue.build());
                return this;
            }

            public Builder addAllFundDruationInfo(java.lang.Iterable<? extends com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo> values) {
                if (result.fundDruationInfo_.isEmpty()) {
                    result.fundDruationInfo_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo>();
                }
                super.addAll(values, result.fundDruationInfo_);
                return this;
            }

            public Builder clearFundDruationInfo() {
                result.fundDruationInfo_ = java.util.Collections.emptyList();
                return this;
            }

            public boolean hasJjdm() {
                return result.hasJjdm();
            }

            public java.lang.String getJjdm() {
                return result.getJjdm();
            }

            public Builder setJjdm(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasJjdm = true;
                result.jjdm_ = value;
                return this;
            }

            public Builder clearJjdm() {
                result.hasJjdm = false;
                result.jjdm_ = getDefaultInstance().getJjdm();
                return this;
            }
        }

        static {
            defaultInstance = new FundDruation(true);
            com.howbuy.wireless.entity.protobuf.FundDruationProtos.internalForceInit();
            defaultInstance.initFields();
        }
    }

    public static final class FundDruationProto extends com.google.protobuf.GeneratedMessage {

        private FundDruationProto() {
            initFields();
        }

        private FundDruationProto(boolean noInit) {
        }

        private static final FundDruationProto defaultInstance;

        public static FundDruationProto getDefaultInstance() {
            return defaultInstance;
        }

        public FundDruationProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.howbuy.wireless.entity.protobuf.FundDruationProtos.internal_static_FundDruationProto_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.howbuy.wireless.entity.protobuf.FundDruationProtos.internal_static_FundDruationProto_fieldAccessorTable;
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

        public static final int FUNDDRUATION_FIELD_NUMBER = 2;

        private java.util.List<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation> fundDruation_ = java.util.Collections.emptyList();

        public java.util.List<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation> getFundDruationList() {
            return fundDruation_;
        }

        public int getFundDruationCount() {
            return fundDruation_.size();
        }

        public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation getFundDruation(int index) {
            return fundDruation_.get(index);
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
            for (com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation element : getFundDruationList()) {
                output.writeMessage(2, element);
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
            for (com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation element : getFundDruationList()) {
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, element);
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto();
                return builder;
            }

            protected com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto.getDescriptor();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto getDefaultInstanceForType() {
                return com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                if (result.fundDruation_ != java.util.Collections.EMPTY_LIST) {
                    result.fundDruation_ = java.util.Collections.unmodifiableList(result.fundDruation_);
                }
                com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto) {
                    return mergeFrom((com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto other) {
                if (other == com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto.getDefaultInstance()) return this;
                if (other.hasCommon()) {
                    mergeCommon(other.getCommon());
                }
                if (!other.fundDruation_.isEmpty()) {
                    if (result.fundDruation_.isEmpty()) {
                        result.fundDruation_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation>();
                    }
                    result.fundDruation_.addAll(other.fundDruation_);
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
                        case 18:
                            {
                                com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.Builder subBuilder = com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.newBuilder();
                                input.readMessage(subBuilder, extensionRegistry);
                                addFundDruation(subBuilder.buildPartial());
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

            public java.util.List<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation> getFundDruationList() {
                return java.util.Collections.unmodifiableList(result.fundDruation_);
            }

            public int getFundDruationCount() {
                return result.getFundDruationCount();
            }

            public com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation getFundDruation(int index) {
                return result.getFundDruation(index);
            }

            public Builder setFundDruation(int index, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.fundDruation_.set(index, value);
                return this;
            }

            public Builder setFundDruation(int index, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.Builder builderForValue) {
                result.fundDruation_.set(index, builderForValue.build());
                return this;
            }

            public Builder addFundDruation(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.fundDruation_.isEmpty()) {
                    result.fundDruation_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation>();
                }
                result.fundDruation_.add(value);
                return this;
            }

            public Builder addFundDruation(com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.Builder builderForValue) {
                if (result.fundDruation_.isEmpty()) {
                    result.fundDruation_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation>();
                }
                result.fundDruation_.add(builderForValue.build());
                return this;
            }

            public Builder addAllFundDruation(java.lang.Iterable<? extends com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation> values) {
                if (result.fundDruation_.isEmpty()) {
                    result.fundDruation_ = new java.util.ArrayList<com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation>();
                }
                super.addAll(values, result.fundDruation_);
                return this;
            }

            public Builder clearFundDruation() {
                result.fundDruation_ = java.util.Collections.emptyList();
                return this;
            }
        }

        static {
            defaultInstance = new FundDruationProto(true);
            com.howbuy.wireless.entity.protobuf.FundDruationProtos.internalForceInit();
            defaultInstance.initFields();
        }
    }

    private static com.google.protobuf.Descriptors.Descriptor internal_static_FundDruationInfo_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_FundDruationInfo_fieldAccessorTable;

    private static com.google.protobuf.Descriptors.Descriptor internal_static_FundDruation_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_FundDruation_fieldAccessorTable;

    private static com.google.protobuf.Descriptors.Descriptor internal_static_FundDruationProto_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_FundDruationProto_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

    static {
        java.lang.String[] descriptorData = { "\n\022fundDuration.proto\032\014common.proto\".\n\020Fu" + "ndDruationInfo\022\014\n\004jzrq\030\001 \001(\t\022\014\n\004jjjz\030\002 \001" + "(\t\"I\n\014FundDruation\022+\n\020fundDruationInfo\030\001" + " \003(\0132\021.FundDruationInfo\022\014\n\004jjdm\030\002 \001(\t\"X\n" + "\021FundDruationProto\022\036\n\006common\030\001 \001(\0132\016.com" + "mon.Common\022#\n\014fundDruation\030\002 \003(\0132\r.FundD" + "ruationB9\n#com.howbuy.wireless.entity.pr" + "otobufB\022FundDruationProtos" };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {

            public com.google.protobuf.ExtensionRegistry assignDescriptors(com.google.protobuf.Descriptors.FileDescriptor root) {
                descriptor = root;
                internal_static_FundDruationInfo_descriptor = getDescriptor().getMessageTypes().get(0);
                internal_static_FundDruationInfo_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_FundDruationInfo_descriptor, new java.lang.String[] { "Jzrq", "Jjjz" }, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.class, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationInfo.Builder.class);
                internal_static_FundDruation_descriptor = getDescriptor().getMessageTypes().get(1);
                internal_static_FundDruation_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_FundDruation_descriptor, new java.lang.String[] { "FundDruationInfo", "Jjdm" }, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.class, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruation.Builder.class);
                internal_static_FundDruationProto_descriptor = getDescriptor().getMessageTypes().get(2);
                internal_static_FundDruationProto_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_FundDruationProto_descriptor, new java.lang.String[] { "Common", "FundDruation" }, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto.class, com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto.Builder.class);
                return null;
            }
        };
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] { com.howbuy.wireless.entity.protobuf.CommonProtos.getDescriptor() }, assigner);
    }

    public static void internalForceInit() {
    }
}
