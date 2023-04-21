package com.phloc.commons.messagedigest;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Base class for creating a cryptographic hash value. Don't mix it up with the
 * {@link com.phloc.commons.hash.HashCodeGenerator} which is used to generate
 * hash values for Java objects.
 * 
 * @author philip
 */
public final class MessageDigestGenerator extends AbstractMessageDigestGenerator {

    private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock();

    private final IMessageDigestGenerator m_aMDGen;

    /**
   * Create a default hash generator with the default algorithm.
   */
    public MessageDigestGenerator() {
        this(DEFAULT_ALGORITHM);
    }

    /**
   * Create a hash generator with a set of possible algorithms to use.
   * 
   * @param aAlgorithms
   *        The parameters to test. May not be <code>null</code>.
   * @throws NullPointerException
   *         If the array of algorithms is <code>null</code> or if one element
   *         of the array is <code>null</code>.
   * @throws IllegalArgumentException
   *         If no algorithm was passed or if no applicable algorithm was used.
   */
    public MessageDigestGenerator(@Nonnull @Nonempty final EMessageDigestAlgorithm... aAlgorithms) {
        m_aMDGen = new NonBlockingMessageDigestGenerator(aAlgorithms);
    }

    @Nonnull
    public String getAlgorithmName() {
        return m_aMDGen.getAlgorithmName();
    }

    @Nonnegative
    public int getDigestLength() {
        return m_aMDGen.getDigestLength();
    }

    @Nonnull
    public MessageDigestGenerator update(final byte aValue) {
        m_aRWLock.writeLock().lock();
        try {
            m_aMDGen.update(aValue);
            return this;
        } finally {
            m_aRWLock.writeLock().unlock();
        }
    }

    @Nonnull
    public MessageDigestGenerator update(@Nonnull final byte[] aValue, @Nonnegative final int nOffset, @Nonnegative final int nLength) {
        m_aRWLock.writeLock().lock();
        try {
            m_aMDGen.update(aValue, nOffset, nLength);
            return this;
        } finally {
            m_aRWLock.writeLock().unlock();
        }
    }

    public void reset() {
        m_aRWLock.writeLock().lock();
        try {
            m_aMDGen.reset();
        } finally {
            m_aRWLock.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public byte[] getDigest() {
        m_aRWLock.writeLock().lock();
        try {
            return m_aMDGen.getDigest();
        } finally {
            m_aRWLock.writeLock().unlock();
        }
    }

    @Nonnull
    @ReturnsMutableCopy
    public byte[] getDigest(@Nonnegative final int nLength) {
        m_aRWLock.writeLock().lock();
        try {
            return m_aMDGen.getDigest(nLength);
        } finally {
            m_aRWLock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        return new ToStringGenerator(this).append("messageDigestGenerator", m_aMDGen).toString();
    }
}
