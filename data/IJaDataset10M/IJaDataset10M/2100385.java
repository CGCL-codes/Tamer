package com.dyuproject.protostuff.benchmark;

public final class V2CodeSizeMedia {

    private V2CodeSizeMedia() {
    }

    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    }

    public static final class Image extends com.google.protobuf.GeneratedMessage {

        private Image() {
            initFields();
        }

        private Image(boolean noInit) {
        }

        private static final Image defaultInstance;

        public static Image getDefaultInstance() {
            return defaultInstance;
        }

        public Image getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internal_static_serializers_protobuf_media_Image_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internal_static_serializers_protobuf_media_Image_fieldAccessorTable;
        }

        public enum Size implements com.google.protobuf.ProtocolMessageEnum {

            SMALL(0, 0), LARGE(1, 1);

            public final int getNumber() {
                return value;
            }

            public static Size valueOf(int value) {
                switch(value) {
                    case 0:
                        return SMALL;
                    case 1:
                        return LARGE;
                    default:
                        return null;
                }
            }

            public static com.google.protobuf.Internal.EnumLiteMap<Size> internalGetValueMap() {
                return internalValueMap;
            }

            private static com.google.protobuf.Internal.EnumLiteMap<Size> internalValueMap = new com.google.protobuf.Internal.EnumLiteMap<Size>() {

                public Size findValueByNumber(int number) {
                    return Size.valueOf(number);
                }
            };

            public final com.google.protobuf.Descriptors.EnumValueDescriptor getValueDescriptor() {
                return getDescriptor().getValues().get(index);
            }

            public final com.google.protobuf.Descriptors.EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public static final com.google.protobuf.Descriptors.EnumDescriptor getDescriptor() {
                return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.getDescriptor().getEnumTypes().get(0);
            }

            private static final Size[] VALUES = { SMALL, LARGE };

            public static Size valueOf(com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new java.lang.IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                return VALUES[desc.getIndex()];
            }

            private final int index;

            private final int value;

            private Size(int index, int value) {
                this.index = index;
                this.value = value;
            }

            static {
                com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.getDescriptor();
            }
        }

        public static final int URI_FIELD_NUMBER = 1;

        private boolean hasUri;

        private java.lang.String uri_ = "";

        public boolean hasUri() {
            return hasUri;
        }

        public java.lang.String getUri() {
            return uri_;
        }

        public static final int TITLE_FIELD_NUMBER = 2;

        private boolean hasTitle;

        private java.lang.String title_ = "";

        public boolean hasTitle() {
            return hasTitle;
        }

        public java.lang.String getTitle() {
            return title_;
        }

        public static final int WIDTH_FIELD_NUMBER = 3;

        private boolean hasWidth;

        private int width_ = 0;

        public boolean hasWidth() {
            return hasWidth;
        }

        public int getWidth() {
            return width_;
        }

        public static final int HEIGHT_FIELD_NUMBER = 4;

        private boolean hasHeight;

        private int height_ = 0;

        public boolean hasHeight() {
            return hasHeight;
        }

        public int getHeight() {
            return height_;
        }

        public static final int SIZE_FIELD_NUMBER = 5;

        private boolean hasSize;

        private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Size size_;

        public boolean hasSize() {
            return hasSize;
        }

        public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Size getSize() {
            return size_;
        }

        private void initFields() {
            size_ = com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Size.SMALL;
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image();
                return builder;
            }

            protected com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.getDescriptor();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image getDefaultInstanceForType() {
                return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image returnMe = result;
                result = null;
                return returnMe;
            }

            public boolean hasUri() {
                return result.hasUri();
            }

            public java.lang.String getUri() {
                return result.getUri();
            }

            public Builder setUri(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasUri = true;
                result.uri_ = value;
                return this;
            }

            public Builder clearUri() {
                result.hasUri = false;
                result.uri_ = getDefaultInstance().getUri();
                return this;
            }

            public boolean hasTitle() {
                return result.hasTitle();
            }

            public java.lang.String getTitle() {
                return result.getTitle();
            }

            public Builder setTitle(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasTitle = true;
                result.title_ = value;
                return this;
            }

            public Builder clearTitle() {
                result.hasTitle = false;
                result.title_ = getDefaultInstance().getTitle();
                return this;
            }

            public boolean hasWidth() {
                return result.hasWidth();
            }

            public int getWidth() {
                return result.getWidth();
            }

            public Builder setWidth(int value) {
                result.hasWidth = true;
                result.width_ = value;
                return this;
            }

            public Builder clearWidth() {
                result.hasWidth = false;
                result.width_ = 0;
                return this;
            }

            public boolean hasHeight() {
                return result.hasHeight();
            }

            public int getHeight() {
                return result.getHeight();
            }

            public Builder setHeight(int value) {
                result.hasHeight = true;
                result.height_ = value;
                return this;
            }

            public Builder clearHeight() {
                result.hasHeight = false;
                result.height_ = 0;
                return this;
            }

            public boolean hasSize() {
                return result.hasSize();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Size getSize() {
                return result.getSize();
            }

            public Builder setSize(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Size value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasSize = true;
                result.size_ = value;
                return this;
            }

            public Builder clearSize() {
                result.hasSize = false;
                result.size_ = com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Size.SMALL;
                return this;
            }
        }

        static {
            defaultInstance = new Image(true);
            com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internalForceInit();
            defaultInstance.initFields();
        }
    }

    public static final class Media extends com.google.protobuf.GeneratedMessage {

        private Media() {
            initFields();
        }

        private Media(boolean noInit) {
        }

        private static final Media defaultInstance;

        public static Media getDefaultInstance() {
            return defaultInstance;
        }

        public Media getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internal_static_serializers_protobuf_media_Media_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internal_static_serializers_protobuf_media_Media_fieldAccessorTable;
        }

        public enum Player implements com.google.protobuf.ProtocolMessageEnum {

            JAVA(0, 0), FLASH(1, 1);

            public final int getNumber() {
                return value;
            }

            public static Player valueOf(int value) {
                switch(value) {
                    case 0:
                        return JAVA;
                    case 1:
                        return FLASH;
                    default:
                        return null;
                }
            }

            public static com.google.protobuf.Internal.EnumLiteMap<Player> internalGetValueMap() {
                return internalValueMap;
            }

            private static com.google.protobuf.Internal.EnumLiteMap<Player> internalValueMap = new com.google.protobuf.Internal.EnumLiteMap<Player>() {

                public Player findValueByNumber(int number) {
                    return Player.valueOf(number);
                }
            };

            public final com.google.protobuf.Descriptors.EnumValueDescriptor getValueDescriptor() {
                return getDescriptor().getValues().get(index);
            }

            public final com.google.protobuf.Descriptors.EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public static final com.google.protobuf.Descriptors.EnumDescriptor getDescriptor() {
                return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.getDescriptor().getEnumTypes().get(0);
            }

            private static final Player[] VALUES = { JAVA, FLASH };

            public static Player valueOf(com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new java.lang.IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                return VALUES[desc.getIndex()];
            }

            private final int index;

            private final int value;

            private Player(int index, int value) {
                this.index = index;
                this.value = value;
            }

            static {
                com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.getDescriptor();
            }
        }

        public static final int URI_FIELD_NUMBER = 1;

        private boolean hasUri;

        private java.lang.String uri_ = "";

        public boolean hasUri() {
            return hasUri;
        }

        public java.lang.String getUri() {
            return uri_;
        }

        public static final int TITLE_FIELD_NUMBER = 2;

        private boolean hasTitle;

        private java.lang.String title_ = "";

        public boolean hasTitle() {
            return hasTitle;
        }

        public java.lang.String getTitle() {
            return title_;
        }

        public static final int WIDTH_FIELD_NUMBER = 3;

        private boolean hasWidth;

        private int width_ = 0;

        public boolean hasWidth() {
            return hasWidth;
        }

        public int getWidth() {
            return width_;
        }

        public static final int HEIGHT_FIELD_NUMBER = 4;

        private boolean hasHeight;

        private int height_ = 0;

        public boolean hasHeight() {
            return hasHeight;
        }

        public int getHeight() {
            return height_;
        }

        public static final int FORMAT_FIELD_NUMBER = 5;

        private boolean hasFormat;

        private java.lang.String format_ = "";

        public boolean hasFormat() {
            return hasFormat;
        }

        public java.lang.String getFormat() {
            return format_;
        }

        public static final int DURATION_FIELD_NUMBER = 6;

        private boolean hasDuration;

        private long duration_ = 0L;

        public boolean hasDuration() {
            return hasDuration;
        }

        public long getDuration() {
            return duration_;
        }

        public static final int SIZE_FIELD_NUMBER = 7;

        private boolean hasSize;

        private long size_ = 0L;

        public boolean hasSize() {
            return hasSize;
        }

        public long getSize() {
            return size_;
        }

        public static final int BITRATE_FIELD_NUMBER = 8;

        private boolean hasBitrate;

        private int bitrate_ = 0;

        public boolean hasBitrate() {
            return hasBitrate;
        }

        public int getBitrate() {
            return bitrate_;
        }

        public static final int PERSON_FIELD_NUMBER = 9;

        private java.util.List<java.lang.String> person_ = java.util.Collections.emptyList();

        public java.util.List<java.lang.String> getPersonList() {
            return person_;
        }

        public int getPersonCount() {
            return person_.size();
        }

        public java.lang.String getPerson(int index) {
            return person_.get(index);
        }

        public static final int PLAYER_FIELD_NUMBER = 10;

        private boolean hasPlayer;

        private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Player player_;

        public boolean hasPlayer() {
            return hasPlayer;
        }

        public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Player getPlayer() {
            return player_;
        }

        public static final int COPYRIGHT_FIELD_NUMBER = 11;

        private boolean hasCopyright;

        private java.lang.String copyright_ = "";

        public boolean hasCopyright() {
            return hasCopyright;
        }

        public java.lang.String getCopyright() {
            return copyright_;
        }

        private void initFields() {
            player_ = com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Player.JAVA;
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media();
                return builder;
            }

            protected com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.getDescriptor();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media getDefaultInstanceForType() {
                return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                if (result.person_ != java.util.Collections.EMPTY_LIST) {
                    result.person_ = java.util.Collections.unmodifiableList(result.person_);
                }
                com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media returnMe = result;
                result = null;
                return returnMe;
            }

            public boolean hasUri() {
                return result.hasUri();
            }

            public java.lang.String getUri() {
                return result.getUri();
            }

            public Builder setUri(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasUri = true;
                result.uri_ = value;
                return this;
            }

            public Builder clearUri() {
                result.hasUri = false;
                result.uri_ = getDefaultInstance().getUri();
                return this;
            }

            public boolean hasTitle() {
                return result.hasTitle();
            }

            public java.lang.String getTitle() {
                return result.getTitle();
            }

            public Builder setTitle(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasTitle = true;
                result.title_ = value;
                return this;
            }

            public Builder clearTitle() {
                result.hasTitle = false;
                result.title_ = getDefaultInstance().getTitle();
                return this;
            }

            public boolean hasWidth() {
                return result.hasWidth();
            }

            public int getWidth() {
                return result.getWidth();
            }

            public Builder setWidth(int value) {
                result.hasWidth = true;
                result.width_ = value;
                return this;
            }

            public Builder clearWidth() {
                result.hasWidth = false;
                result.width_ = 0;
                return this;
            }

            public boolean hasHeight() {
                return result.hasHeight();
            }

            public int getHeight() {
                return result.getHeight();
            }

            public Builder setHeight(int value) {
                result.hasHeight = true;
                result.height_ = value;
                return this;
            }

            public Builder clearHeight() {
                result.hasHeight = false;
                result.height_ = 0;
                return this;
            }

            public boolean hasFormat() {
                return result.hasFormat();
            }

            public java.lang.String getFormat() {
                return result.getFormat();
            }

            public Builder setFormat(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasFormat = true;
                result.format_ = value;
                return this;
            }

            public Builder clearFormat() {
                result.hasFormat = false;
                result.format_ = getDefaultInstance().getFormat();
                return this;
            }

            public boolean hasDuration() {
                return result.hasDuration();
            }

            public long getDuration() {
                return result.getDuration();
            }

            public Builder setDuration(long value) {
                result.hasDuration = true;
                result.duration_ = value;
                return this;
            }

            public Builder clearDuration() {
                result.hasDuration = false;
                result.duration_ = 0L;
                return this;
            }

            public boolean hasSize() {
                return result.hasSize();
            }

            public long getSize() {
                return result.getSize();
            }

            public Builder setSize(long value) {
                result.hasSize = true;
                result.size_ = value;
                return this;
            }

            public Builder clearSize() {
                result.hasSize = false;
                result.size_ = 0L;
                return this;
            }

            public boolean hasBitrate() {
                return result.hasBitrate();
            }

            public int getBitrate() {
                return result.getBitrate();
            }

            public Builder setBitrate(int value) {
                result.hasBitrate = true;
                result.bitrate_ = value;
                return this;
            }

            public Builder clearBitrate() {
                result.hasBitrate = false;
                result.bitrate_ = 0;
                return this;
            }

            public java.util.List<java.lang.String> getPersonList() {
                return java.util.Collections.unmodifiableList(result.person_);
            }

            public int getPersonCount() {
                return result.getPersonCount();
            }

            public java.lang.String getPerson(int index) {
                return result.getPerson(index);
            }

            public Builder setPerson(int index, java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.person_.set(index, value);
                return this;
            }

            public Builder addPerson(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.person_.isEmpty()) {
                    result.person_ = new java.util.ArrayList<java.lang.String>();
                }
                result.person_.add(value);
                return this;
            }

            public Builder addAllPerson(java.lang.Iterable<? extends java.lang.String> values) {
                if (result.person_.isEmpty()) {
                    result.person_ = new java.util.ArrayList<java.lang.String>();
                }
                super.addAll(values, result.person_);
                return this;
            }

            public Builder clearPerson() {
                result.person_ = java.util.Collections.emptyList();
                return this;
            }

            public boolean hasPlayer() {
                return result.hasPlayer();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Player getPlayer() {
                return result.getPlayer();
            }

            public Builder setPlayer(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Player value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasPlayer = true;
                result.player_ = value;
                return this;
            }

            public Builder clearPlayer() {
                result.hasPlayer = false;
                result.player_ = com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Player.JAVA;
                return this;
            }

            public boolean hasCopyright() {
                return result.hasCopyright();
            }

            public java.lang.String getCopyright() {
                return result.getCopyright();
            }

            public Builder setCopyright(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasCopyright = true;
                result.copyright_ = value;
                return this;
            }

            public Builder clearCopyright() {
                result.hasCopyright = false;
                result.copyright_ = getDefaultInstance().getCopyright();
                return this;
            }
        }

        static {
            defaultInstance = new Media(true);
            com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internalForceInit();
            defaultInstance.initFields();
        }
    }

    public static final class MediaContent extends com.google.protobuf.GeneratedMessage {

        private MediaContent() {
            initFields();
        }

        private MediaContent(boolean noInit) {
        }

        private static final MediaContent defaultInstance;

        public static MediaContent getDefaultInstance() {
            return defaultInstance;
        }

        public MediaContent getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internal_static_serializers_protobuf_media_MediaContent_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internal_static_serializers_protobuf_media_MediaContent_fieldAccessorTable;
        }

        public static final int IMAGE_FIELD_NUMBER = 1;

        private java.util.List<com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image> image_ = java.util.Collections.emptyList();

        public java.util.List<com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image> getImageList() {
            return image_;
        }

        public int getImageCount() {
            return image_.size();
        }

        public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image getImage(int index) {
            return image_.get(index);
        }

        public static final int MEDIA_FIELD_NUMBER = 2;

        private boolean hasMedia;

        private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media media_;

        public boolean hasMedia() {
            return hasMedia;
        }

        public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media getMedia() {
            return media_;
        }

        private void initFields() {
            media_ = com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.getDefaultInstance();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent();
                return builder;
            }

            protected com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent.getDescriptor();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent getDefaultInstanceForType() {
                return com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                if (result.image_ != java.util.Collections.EMPTY_LIST) {
                    result.image_ = java.util.Collections.unmodifiableList(result.image_);
                }
                com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent returnMe = result;
                result = null;
                return returnMe;
            }

            public java.util.List<com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image> getImageList() {
                return java.util.Collections.unmodifiableList(result.image_);
            }

            public int getImageCount() {
                return result.getImageCount();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image getImage(int index) {
                return result.getImage(index);
            }

            public Builder setImage(int index, com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.image_.set(index, value);
                return this;
            }

            public Builder setImage(int index, com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Builder builderForValue) {
                result.image_.set(index, builderForValue.build());
                return this;
            }

            public Builder addImage(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.image_.isEmpty()) {
                    result.image_ = new java.util.ArrayList<com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image>();
                }
                result.image_.add(value);
                return this;
            }

            public Builder addImage(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Builder builderForValue) {
                if (result.image_.isEmpty()) {
                    result.image_ = new java.util.ArrayList<com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image>();
                }
                result.image_.add(builderForValue.build());
                return this;
            }

            public Builder addAllImage(java.lang.Iterable<? extends com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image> values) {
                if (result.image_.isEmpty()) {
                    result.image_ = new java.util.ArrayList<com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image>();
                }
                super.addAll(values, result.image_);
                return this;
            }

            public Builder clearImage() {
                result.image_ = java.util.Collections.emptyList();
                return this;
            }

            public boolean hasMedia() {
                return result.hasMedia();
            }

            public com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media getMedia() {
                return result.getMedia();
            }

            public Builder setMedia(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasMedia = true;
                result.media_ = value;
                return this;
            }

            public Builder setMedia(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Builder builderForValue) {
                result.hasMedia = true;
                result.media_ = builderForValue.build();
                return this;
            }

            public Builder mergeMedia(com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media value) {
                if (result.hasMedia() && result.media_ != com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.getDefaultInstance()) {
                    result.media_ = com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.newBuilder(result.media_).mergeFrom(value).buildPartial();
                } else {
                    result.media_ = value;
                }
                result.hasMedia = true;
                return this;
            }

            public Builder clearMedia() {
                result.hasMedia = false;
                result.media_ = com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.getDefaultInstance();
                return this;
            }
        }

        static {
            defaultInstance = new MediaContent(true);
            com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.internalForceInit();
            defaultInstance.initFields();
        }
    }

    private static com.google.protobuf.Descriptors.Descriptor internal_static_serializers_protobuf_media_Image_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_serializers_protobuf_media_Image_fieldAccessorTable;

    private static com.google.protobuf.Descriptors.Descriptor internal_static_serializers_protobuf_media_Media_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_serializers_protobuf_media_Media_fieldAccessorTable;

    private static com.google.protobuf.Descriptors.Descriptor internal_static_serializers_protobuf_media_MediaContent_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_serializers_protobuf_media_MediaContent_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

    static {
        java.lang.String[] descriptorData = { "\n\036src/main/resources/media.proto\022\032serial" + "izers.protobuf.media\"\226\001\n\005Image\022\013\n\003uri\030\001 " + "\002(\t\022\r\n\005title\030\002 \001(\t\022\r\n\005width\030\003 \002(\005\022\016\n\006hei" + "ght\030\004 \002(\005\0224\n\004size\030\005 \002(\0162&.serializers.pr" + "otobuf.media.Image.Size\"\034\n\004Size\022\t\n\005SMALL" + "\020\000\022\t\n\005LARGE\020\001\"\377\001\n\005Media\022\013\n\003uri\030\001 \002(\t\022\r\n\005" + "title\030\002 \001(\t\022\r\n\005width\030\003 \002(\005\022\016\n\006height\030\004 \002" + "(\005\022\016\n\006format\030\005 \002(\t\022\020\n\010duration\030\006 \002(\003\022\014\n\004" + "size\030\007 \002(\003\022\017\n\007bitrate\030\010 \001(\005\022\016\n\006person\030\t " + "\003(\t\0228\n\006player\030\n \002(\0162(.serializers.protob", "uf.media.Media.Player\022\021\n\tcopyright\030\013 \001(\t" + "\"\035\n\006Player\022\010\n\004JAVA\020\000\022\t\n\005FLASH\020\001\"r\n\014Media" + "Content\0220\n\005image\030\001 \003(\0132!.serializers.pro" + "tobuf.media.Image\0220\n\005media\030\002 \002(\0132!.seria" + "lizers.protobuf.media.MediaB8\n#com.dyupr" + "oject.protostuff.benchmarkB\017V2CodeSizeMe" + "diaH\002" };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {

            public com.google.protobuf.ExtensionRegistry assignDescriptors(com.google.protobuf.Descriptors.FileDescriptor root) {
                descriptor = root;
                internal_static_serializers_protobuf_media_Image_descriptor = getDescriptor().getMessageTypes().get(0);
                internal_static_serializers_protobuf_media_Image_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_serializers_protobuf_media_Image_descriptor, new java.lang.String[] { "Uri", "Title", "Width", "Height", "Size" }, com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.class, com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Builder.class);
                internal_static_serializers_protobuf_media_Media_descriptor = getDescriptor().getMessageTypes().get(1);
                internal_static_serializers_protobuf_media_Media_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_serializers_protobuf_media_Media_descriptor, new java.lang.String[] { "Uri", "Title", "Width", "Height", "Format", "Duration", "Size", "Bitrate", "Person", "Player", "Copyright" }, com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.class, com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Builder.class);
                internal_static_serializers_protobuf_media_MediaContent_descriptor = getDescriptor().getMessageTypes().get(2);
                internal_static_serializers_protobuf_media_MediaContent_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_serializers_protobuf_media_MediaContent_descriptor, new java.lang.String[] { "Image", "Media" }, com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent.class, com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent.Builder.class);
                return null;
            }
        };
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] {}, assigner);
    }

    public static void internalForceInit() {
    }
}
