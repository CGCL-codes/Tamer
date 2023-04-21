package jpfm;

/**
 *
 * @author Shashank Tulsyan
 */
public final class VolumeFlags {

    public static final VolumeFlags SIMPLE_FLAG = new VolumeFlags(0);

    private final int volumeFlags;

    VolumeFlags(final int volumeFlag) {
        this.volumeFlags = volumeFlag;
    }

    public int getVolumeFlags() {
        return volumeFlags;
    }

    public final boolean isCaseSensitive() {
        return (volumeFlags & 0x08) == 0x08;
    }

    public final boolean isCompressed() {
        return (volumeFlags & 0x02) == 0x02;
    }

    public final boolean isEncrypted() {
        return (volumeFlags & 0x04) == 0x04;
    }

    public final boolean isReadOnly() {
        return (volumeFlags & 0x01) == 0x01;
    }

    public static final class Builder {

        private int volumeFlags = 0;

        public Builder() {
        }

        public final boolean isCaseSensitive() {
            return (volumeFlags & 0x08) == 0x08;
        }

        public final boolean isCompressed() {
            return (volumeFlags & 0x02) == 0x02;
        }

        public final boolean isEncrypted() {
            return (volumeFlags & 0x04) == 0x04;
        }

        public final boolean isReadOnly() {
            return (volumeFlags & 0x01) == 0x01;
        }

        public final Builder setCaseSensitive() {
            volumeFlags |= 0x08;
            return this;
        }

        public final Builder setCompressed() {
            volumeFlags |= 0x02;
            return this;
        }

        public final Builder setEncrypted() {
            volumeFlags |= 0x04;
            return this;
        }

        public final Builder setReadOnly() {
            volumeFlags |= 0x01;
            return this;
        }

        public final VolumeFlags build() {
            return new VolumeFlags(volumeFlags);
        }
    }
}
