package de.ibk.ods.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.asam.ods.AoException;
import org.asam.ods.DataType;
import org.asam.ods.ErrorCode;
import org.asam.ods.InstanceElement;
import org.asam.ods.InstanceElementIterator;
import org.asam.ods.NameValueUnit;
import org.asam.ods.Relationship;
import org.asam.ods.SeverityFlag;
import org.asam.ods.TS_Union;
import org.asam.ods.TS_Value;

/**
 * @author Reinhard Kessler, Ingenieurbüro Kessler
 * @version 5.0.0
 */
public class SvcValHelper {

    /**
	 * 
	 */
    private Logger log = LogManager.getLogger("de.ibk.ods.openaos");

    /**
	 * 
	 */
    private Connection connection;

    private InstanceElement instElem;

    private DataType dataType;

    private String meqid;

    private String pmatnum;

    private int dataCount = 0;

    private TS_Union values = new TS_Union();

    private TS_Union flags = new TS_Union();

    /**
	 * @param connection
	 * @param elem
	 */
    public SvcValHelper(Connection connection, InstanceElement elem) throws AoException {
        super();
        log.debug("Enter SvcValHelper::SvcValHelper()");
        this.connection = connection;
        this.instElem = elem;
        InstanceElement ie = null;
        InstanceElementIterator iei = instElem.getRelatedInstancesByRelationship(Relationship.INFO_FROM, "*");
        int n = iei.getCount();
        if (n == 0) {
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "InstanceElement::getValue()");
        }
        InstanceElement[] instances = iei.nextN(n);
        for (int i = 0; i < instances.length; i++) {
            if ("AoMeasurementQuantity".equals(instances[i].getApplicationElement().getBaseElement().getType())) {
                ie = instances[i];
                break;
            }
        }
        iei.destroy();
        if (ie == null) {
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "SvcValHelper::SvcValHelper()");
        }
        this.meqid = Integer.toString(ie.getId().low);
        NameValueUnit nvu = ie.getValueByBaseName("datatype");
        int id = 0;
        switch(nvu.value.u.discriminator().value()) {
            case DataType._DT_LONG:
                id = nvu.value.u.longVal();
                break;
            case DataType._DT_ENUM:
                id = nvu.value.u.enumVal();
                break;
            default:
                break;
        }
        this.dataType = DataType.from_int(id);
        iei = instElem.getRelatedInstancesByRelationship(Relationship.FATHER, "*");
        if (iei.getCount() != 1) {
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "SvcValHelper::SvcValHelper()");
        }
        ie = iei.nextOne();
        iei.destroy();
        this.pmatnum = Integer.toString(ie.getId().low);
        String sql = SqlHelper.format("select VALBLOBLEN from SVCVAL where MEQID=%s and PMATNUM=%s", new String[] { meqid, pmatnum });
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                this.dataCount += rs.getInt(1);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.fatal(e.getMessage());
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "SvcValHelper::SvcValHelper()");
        }
        log.debug("Exit SvcValHelper::SvcValHelper()");
    }

    /**
	 * 
	 * @param baName
	 * @return
	 * @throws AoException
	 */
    public NameValueUnit getValues(String baName) throws AoException {
        log.debug("Enter SvcValHelper::getValues()");
        this.readData();
        NameValueUnit retval = new NameValueUnit();
        retval.valName = baName;
        retval.unit = "";
        retval.value = new TS_Value();
        retval.value.flag = (short) 15;
        if ("flags".equals(baName)) {
            retval.value.u = this.flags;
        } else {
            if ("values".equals(baName)) {
                retval.value.u = this.values;
            } else {
                throw new AoException(ErrorCode.AO_BAD_PARAMETER, SeverityFlag.ERROR, 0, "SvcValHelper::getValues()");
            }
        }
        log.debug("Exit SvcValHelper::getValues()");
        return retval;
    }

    /**
	 * 
	 * @param baName
	 * @param values
	 * @throws AoException
	 */
    public void setValues(String baName, NameValueUnit values) throws AoException {
        log.debug("Enter SvcValHelper::setValues()");
        if (!dataType.equals(values.value.u.discriminator())) {
            log.error("DataType does not match in SvcValHelper::setValues()!");
            throw new AoException(ErrorCode.AO_BAD_PARAMETER, SeverityFlag.ERROR, 0, "SvcValHelper::setValues()");
        }
        this.readData();
        String sql = SqlHelper.format("delete * from SVCVAL where MEQID=%s and PMATNUM=%s", new String[] { meqid, pmatnum });
        QueryHandler qh = new QueryHandler(this.connection);
        try {
            qh.executeUpdate(sql);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "SvcValHelper::setValues()");
        }
        this.prepareData(baName, values);
        this.writeData();
        log.debug("Exit SvcValHelper::setValues()");
    }

    /**
	 * 
	 *
	 */
    private void fillupFlags() {
        log.debug("Enter SvcValHelper::fillupFlags()");
        short[] oldarray = this.flags.shortSeq();
        short[] newarray = new short[this.dataCount];
        int n = oldarray.length;
        int i = 0;
        while (i < this.dataCount) {
            if (i < n) {
                newarray[i] = oldarray[i];
            } else {
                newarray[i] = (short) 15;
            }
            i++;
        }
        this.flags.shortSeq(newarray);
        log.debug("Exit SvcValHelper::fillupFlags()");
    }

    /**
	 * 
	 *
	 */
    private void fillupValues() {
        log.debug("Enter SvcValHelper::fillupValues()");
        int n = 0;
        int i = 0;
        switch(this.values.discriminator().value()) {
            case DataType._DS_DOUBLE:
                double[] olddouble = this.values.doubleSeq();
                double[] newdouble = new double[this.dataCount];
                n = olddouble.length;
                while (i < this.dataCount) {
                    if (i < n) {
                        newdouble[i] = olddouble[i];
                    } else {
                        newdouble[i] = 0.0;
                    }
                    i++;
                }
                this.values.doubleSeq(newdouble);
                break;
            case DataType._DS_FLOAT:
                float[] oldfloat = this.values.floatSeq();
                float[] newfloat = new float[this.dataCount];
                n = oldfloat.length;
                while (i < this.dataCount) {
                    if (i < n) {
                        newfloat[i] = oldfloat[i];
                    } else {
                        newfloat[i] = (float) 0.0;
                    }
                    i++;
                }
                this.values.floatSeq(newfloat);
                break;
        }
        log.debug("Exit SvcValHelper::fillupValues()");
    }

    /**
	 * 
	 * @param baname
	 * @param values
	 * @throws AoException
	 */
    private void prepareData(String baname, NameValueUnit values) throws AoException {
        log.debug("Enter SvcValHelper::prepareData()");
        if ("flags".equals(baname)) {
            this.flags = values.value.u;
        } else {
            this.values = values.value.u;
        }
        int valuecnt = 0;
        switch(this.values.discriminator().value()) {
            case DataType._DS_DOUBLE:
                valuecnt = this.values.doubleSeq().length;
                break;
            case DataType._DS_FLOAT:
                valuecnt = this.values.floatSeq().length;
                break;
        }
        int flagcnt = this.flags.shortSeq().length;
        if (flagcnt < valuecnt) {
            this.dataCount = valuecnt;
            fillupFlags();
        } else {
            this.dataCount = flagcnt;
            if (flagcnt > valuecnt) {
                fillupValues();
            }
        }
        log.debug("Exit SvcValHelper::prepareData()");
    }

    /**
	 * 
	 * @throws AoException
	 */
    private synchronized void readData() throws AoException {
        log.debug("Enter SvcValHelper::readData()");
        String sql = SqlHelper.format("select * from SVCVAL where MEQID=%s and PMATNUM=%s order by SEGNUM", new String[] { meqid, pmatnum });
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            short[] flagarray = new short[this.dataCount];
            int flagpos = 0;
            int valpos = 0;
            switch(dataType.value()) {
                case DataType._DT_DOUBLE:
                case DataType._DS_DOUBLE:
                    double[] darray = new double[this.dataCount];
                    while (rs.next()) {
                        int size = 8;
                        int valbloblen = rs.getInt(6);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rs.getBytes(7));
                        byte[] buf;
                        int length = 0;
                        int cnt = byteArrayInputStream.available();
                        if (cnt > (valbloblen * size)) {
                            buf = new byte[4];
                            byteArrayInputStream.read(buf);
                            for (int i = 0; i < 4; i++) {
                                length += buf[i] & 0xFF;
                                if (i < 3) {
                                    length = length << 1;
                                }
                            }
                        } else {
                            length = cnt;
                        }
                        buf = new byte[size];
                        for (int i = 0; i < valbloblen; i++) {
                            byteArrayInputStream.read(buf);
                            long lval = 0;
                            for (int ii = size - 1; ii >= 0; ii--) {
                                lval = (lval << 8) | (buf[ii] & 0xFF);
                            }
                            darray[valpos + i] = Double.longBitsToDouble(lval);
                        }
                        for (int i = 0; i < valbloblen; i++) {
                            flagarray[flagpos + i] = 15;
                        }
                        flagpos += valbloblen;
                        valpos += valbloblen;
                    }
                    this.values.doubleSeq(darray);
                    break;
                case DataType._DT_FLOAT:
                case DataType._DS_FLOAT:
                    float[] farray = new float[this.dataCount];
                    while (rs.next()) {
                        int size = 4;
                        int valbloblen = rs.getInt(6);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rs.getBytes(7));
                        byte[] buf;
                        int length = 0;
                        int cnt = byteArrayInputStream.available();
                        if (cnt > (valbloblen * size)) {
                            buf = new byte[4];
                            byteArrayInputStream.read(buf);
                            for (int i = 0; i < 4; i++) {
                                length += buf[i] & 0xFF;
                                if (i < 3) {
                                    length = length << 1;
                                }
                            }
                        } else {
                            length = cnt;
                        }
                        buf = new byte[size];
                        for (int i = 0; i < valbloblen; i++) {
                            byteArrayInputStream.read(buf);
                            int ival = 0;
                            for (int ii = size - 1; ii >= 0; ii--) {
                                ival = (ival << 8) | (buf[ii] & 0xFF);
                            }
                            farray[valpos + i] = Float.intBitsToFloat(ival);
                        }
                        for (int i = 0; i < valbloblen; i++) {
                            flagarray[flagpos + i] = 15;
                        }
                        flagpos += valbloblen;
                        valpos += valbloblen;
                    }
                    this.values.floatSeq(farray);
                    break;
            }
            rs.close();
            stmt.close();
            this.flags.shortSeq(flagarray);
            log.debug("Exit SvcValHelper::readData()");
        } catch (SQLException e) {
            log.fatal(e.getMessage());
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "SvcValHelper::readData()");
        } catch (IOException e) {
            log.fatal(e.getMessage());
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "SvcValHelper::readData()");
        }
    }

    /**
	 * 
	 * @throws AoException
	 */
    private void writeData() throws AoException {
        log.debug("Enter SvcValHelper::writeData()");
        int valcnt = 0;
        switch(this.values.discriminator().value()) {
            case DataType._DS_BYTE:
                valcnt = (int) Math.floor(10000 / (1 + 2));
                break;
            case DataType._DS_SHORT:
                valcnt = (int) Math.floor(10000 / (2 + 2));
                break;
            case DataType._DS_LONG:
                valcnt = (int) Math.floor(10000 / (4 + 2));
                break;
            case DataType._DS_FLOAT:
                valcnt = (int) Math.floor(10000 / (4 + 2));
                writeFloatSeq(valcnt);
                break;
            case DataType._DS_DOUBLE:
                valcnt = (int) Math.floor(10000 / (6 + 2));
                writeDoubleSeq(valcnt);
                break;
        }
        log.debug("Exit SvcValHelper::writeData()");
    }

    /**
	 * 
	 * @param valPerRow
	 */
    private synchronized void writeDoubleSeq(int valPerRow) {
        log.debug("Enter SvcValHelper::writeDoubleSeq()");
        int rowcnt = 1;
        String sql = SqlHelper.format("insert into SVCVAL values(%s,%s,?,0,1,?,?)", new String[] { meqid, pmatnum });
        try {
            double[] darray = this.values.doubleSeq();
            short[] flagarray = this.flags.shortSeq();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            int i = 0;
            int ii = 0;
            int pflag = i;
            while (i < this.dataCount) {
                if (ii == 0) {
                    int length = 0;
                    if ((i + valPerRow) > this.dataCount) {
                        length = (this.dataCount - i) * (6 + 2);
                    } else {
                        length = valPerRow * (6 + 2);
                    }
                    dos.writeInt(length);
                }
                dos.writeDouble(darray[i++]);
                ii++;
                if ((i == this.dataCount) || (ii == valPerRow)) {
                    while (pflag < i) {
                        dos.writeShort(flagarray[pflag++]);
                    }
                    dos.flush();
                    PreparedStatement stmt = this.connection.prepareStatement(sql);
                    stmt.setInt(1, rowcnt);
                    stmt.setInt(2, ii);
                    stmt.setAsciiStream(3, new ByteArrayInputStream(baos.toByteArray()), baos.size());
                    stmt.executeUpdate();
                    stmt.close();
                    baos.reset();
                    ii = 0;
                    rowcnt++;
                }
            }
        } catch (IOException e) {
            log.fatal(e.getMessage());
        } catch (SQLException e) {
            log.fatal(e.getMessage());
        }
        log.debug("Exit SvcValHelper::writeDoubleSeq()");
    }

    /**
	 * 
	 * @param valPerRow
	 */
    private synchronized void writeFloatSeq(int valPerRow) {
        log.debug("Enter SvcValHelper::writeFloatSeq()");
        int rowcnt = 1;
        String sql = SqlHelper.format("insert into SVCVAL values(%s,%s,?,0,1,?,?)", new String[] { meqid, pmatnum });
        try {
            float[] farray = this.values.floatSeq();
            short[] flagarray = this.flags.shortSeq();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            int i = 0;
            int ii = 0;
            int pflag = i;
            while (i < this.dataCount) {
                if (ii == 0) {
                    int length = 0;
                    if ((i + valPerRow) > this.dataCount) {
                        length = (this.dataCount - i) * (4 + 2);
                    } else {
                        length = valPerRow * (4 + 2);
                    }
                    dos.writeInt(length);
                }
                dos.writeFloat(farray[i++]);
                ii++;
                if ((i == this.dataCount) || (ii == valPerRow)) {
                    while (pflag < i) {
                        dos.writeShort(flagarray[pflag++]);
                    }
                    dos.flush();
                    PreparedStatement stmt = this.connection.prepareStatement(sql);
                    stmt.setInt(1, rowcnt);
                    stmt.setInt(2, ii);
                    stmt.setAsciiStream(3, new ByteArrayInputStream(baos.toByteArray()), baos.size());
                    stmt.executeUpdate();
                    stmt.close();
                    baos.reset();
                    ii = 0;
                    rowcnt++;
                }
            }
        } catch (IOException e) {
            log.fatal(e.getMessage());
        } catch (SQLException e) {
            log.fatal(e.getMessage());
        }
        log.debug("Exit SvcValHelper::writeFloatSeq()");
    }
}
