package atp.reporter.items.allocators;

import atp.reporter.data.RArguments;
import atp.reporter.data.RDataItem;
import atp.reporter.data.ROKOUni;
import atp.reporter.db.RDBQuery;
import atp.reporter.exception.RParseException;
import atp.reporter.exception.RPrepareException;
import atp.reporter.items.*;
import atp.reporter.product.RLineKey;
import atp.reporter.product.RLineKeyContainer;

/**
 * @author Shteinke_KE
 * ������ ��� ���� ������ ��� ������� �����
 */
public abstract class RAllocatorCount extends RAllocator {

    abstract static class RLineKeyNumber implements RLineKey {

        final int number;

        RLineKeyNumber(int number) {
            this.number = number;
        }

        public boolean equals(Object obj) {
            return obj instanceof RLineKeyNumber && ((RLineKeyNumber) obj).number == number;
        }

        public int hashCode() {
            return number;
        }

        public String toString() {
            return "KEY: number=" + number;
        }
    }

    abstract static class RLineKeyContainerCount extends RLineKeyContainer {

        public RLineKey getLineKey(RDataItem dataItem) {
            final int number = getColumnNumber(dataItem.uni, dataItem.value.value, dataItem.value.date);
            return createLineKey(number);
        }

        /**
		 * �������� ����� ��������� ��� ���������� ����
		 * @param number ����� ���� 
		 * @return ����� ����������
		 */
        protected abstract String getLineHeader(int number);

        public RDBQuery[] getDBQueries(RArguments arguments) throws RPrepareException {
            return EMPTY_QUERIES;
        }

        /**
		 * �������� ����� ����
		 * @param uni TODO
		 * @param value ��������
		 * @param date ����� ��������
		 * @return ����� ����
		 */
        protected abstract int getColumnNumber(ROKOUni uni, Object value, long date);

        protected RLineKey createLineKey(int number) {
            return new RLineKeyNumber(number) {

                public String getTitle() {
                    return getLineHeader(number);
                }
            };
        }
    }

    protected void onClear() {
    }

    public void checkCompleteItem() throws RParseException {
    }
}
