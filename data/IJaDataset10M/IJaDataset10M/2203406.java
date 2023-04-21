package mikhail_barg.ctools.converters.internal;

import java.util.LinkedList;
import mikhail_barg.ctools.colors.Color;
import mikhail_barg.ctools.converters.AbstractConverter;
import mikhail_barg.ctools.converters.ColorConverter;

public class ChainColorConverter<TSource extends Color<TSource>, TResult extends Color<TResult>> extends AbstractConverter<TSource, TResult> {

    private static class ChainLink<TSrc extends Color<TSrc>, TRes extends Color<TRes>> {

        private final ColorConverter<TSrc, TRes> m_converter;

        private final TRes m_resColor;

        public ChainLink(ColorConverter<TSrc, TRes> converter) {
            m_converter = converter;
            try {
                m_resColor = converter.getResultClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public Class<? extends TRes> getResultClass() {
            return m_converter.getResultClass();
        }

        public Class<? extends TSrc> getSourceClass() {
            return m_converter.getSourceClass();
        }

        public TRes convert(Color<?> src) {
            m_converter.convert(m_converter.getSourceClass().cast(src), m_resColor);
            return m_resColor;
        }
    }

    public ChainColorConverter(Class<? extends TSource> sourceClass, Class<? extends TResult> resultClass) {
        super(sourceClass, resultClass);
    }

    private LinkedList<ChainLink<? extends Color<?>, ? extends Color<?>>> m_chain = new LinkedList<ChainLink<? extends Color<?>, ? extends Color<?>>>();

    public <TSrc extends Color<TSrc>, TRes extends Color<TRes>> void addChainLinkToTheEnd(ColorConverter<TSrc, TRes> converter) {
        if (m_chain.size() == 0) {
            if (!m_sourceClass.equals(converter.getSourceClass())) {
                throw new RuntimeException("Source class for the first chain link(" + converter.getSourceClass() + ") does not match to chain converter source class (" + m_sourceClass + ")");
            }
        } else {
            if (!m_chain.getLast().getResultClass().equals(converter.getSourceClass())) {
                throw new RuntimeException("Source class for the new chain link(" + converter.getSourceClass() + ") does not match to previous converter result class (" + m_chain.getLast().getResultClass() + ")");
            }
        }
        m_chain.add(new ChainLink<TSrc, TRes>(converter));
    }

    public <TSrc extends Color<TSrc>, TRes extends Color<TRes>> void addChainLinkToTheStart(ColorConverter<TSrc, TRes> converter) {
        if (m_chain.size() == 0) {
            if (!m_resultClass.equals(converter.getResultClass())) {
                throw new RuntimeException("Result class for the last chain link(" + converter.getResultClass() + ") does not match to chain converter result class (" + m_resultClass + ")");
            }
        } else {
            if (!m_chain.getFirst().getSourceClass().equals(converter.getResultClass())) {
                throw new RuntimeException("Result class for the new chain link(" + converter.getResultClass() + ") does not match to next converter source class (" + m_chain.getFirst().getSourceClass() + ")");
            }
        }
        m_chain.addFirst(new ChainLink<TSrc, TRes>(converter));
    }

    @Override
    public void convert(TSource src, TResult res) {
        if (m_chain.size() < 1) {
            throw new RuntimeException("Empty chain converter " + this);
        }
        Color<?> c = src;
        for (ChainLink<? extends Color<?>, ? extends Color<?>> link : m_chain) {
            c = link.convert(c);
        }
        @SuppressWarnings("unchecked") TResult r = (TResult) c;
        res.assign(r);
    }
}
