package edu.tsinghua.eea.powermanagement.control;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import edu.tsinghua.eea.powermanagement.data.PUData;
import edu.tsinghua.eea.powermanagement.data.XMLData;
import edu.tsinghua.eea.powermanagement.data.XMLPowerData;
import edu.tsinghua.eea.powermanagement.data.XMLPowerDataCell;
import edu.tsinghua.eea.powermanagement.plot.BarCompareView;
import edu.tsinghua.eea.powermanagement.plot.PlotView;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class XmlLoader extends AsyncTask<String, Integer, XMLData> {

    private ProgressDialog mProgDiag;

    private TextView mMessage;

    private PlotView mPlotView;

    private BarCompareView mBarCompareView;

    public XmlLoader(Context context, TextView msg, PlotView pv, BarCompareView bcv) {
        mMessage = msg;
        mProgDiag = new ProgressDialog(context, 0);
        mProgDiag.setCancelable(true);
        mProgDiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgDiag.setMessage("Loading XML...");
        mProgDiag.show();
        mPlotView = pv;
        mBarCompareView = bcv;
    }

    @Override
    protected XMLData doInBackground(String... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(params[0]);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            long length = entity.getContentLength();
            InputStream is = entity.getContent();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(is);
            NodeList nlRoot = doc.getElementsByTagName("power");
            return xmlToData(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(XMLData result) {
        super.onPostExecute(result);
        mProgDiag.dismiss();
        if (result == null) {
            Log.e("XmlData", "Xml data is null!");
            return;
        }
        addMessage("State:" + result.mUserStateId + "\n");
        addMessage("Data size:" + result.mPowerData.mDataSize + "\n");
        addMessage("Vector size:" + result.mPowerData.mPowerVector.size() + "\n");
        PUData pdata = result.getPUData();
        mPlotView.setPUData(pdata);
        mBarCompareView.setData(BarCompareView.CURRENT_DATA_INDEX, pdata.getEnergyCostInPeriods());
    }

    private void addMessage(String s) {
        if (mMessage != null) mMessage.append(s);
    }

    private XMLData xmlToData(Document doc) {
        XMLData data = new XMLData();
        NodeList nlUser = doc.getElementsByTagName("user");
        int iStateId = 0;
        if (nlUser.getLength() > 0) {
            String sSid = nlUser.item(0).getAttributes().getNamedItem("sid").getNodeValue();
            Log.d("XmlData", "String SID = " + sSid);
            iStateId = Integer.parseInt(sSid);
            Log.d("XmlData", "int SID = " + iStateId);
        }
        data.mUserStateId = iStateId;
        NodeList nlPC = doc.getElementsByTagName("power-detail");
        if (nlPC.getLength() > 0) {
            int iSize = 0;
            iSize = Integer.parseInt(nlPC.item(0).getAttributes().getNamedItem("length").getNodeValue());
            XMLPowerData pd = new XMLPowerData();
            pd.mDataSize = iSize;
            NodeList nlCells = nlPC.item(0).getChildNodes();
            for (int i = 0; i < nlCells.getLength(); ++i) {
                if (i > pd.mDataSize) break;
                Node n = nlCells.item(2 * i + 1);
                if (n == null) break;
                NamedNodeMap nAttr = n.getAttributes();
                Log.d("XmlCell", "Cell node:" + i + " data:" + n.getTextContent());
                if (nAttr == null) {
                    Log.d("XmlCell", "Cell node:" + i + " attribute is null!");
                    continue;
                }
                for (int k = 0; k < nAttr.getLength(); ++k) Log.d("XmlCell", "i=" + i + ", attr name=" + nAttr.item(k).getNodeName() + ", attr value=" + nAttr.item(k).getNodeValue());
                String sDate = nAttr.getNamedItem("date").getNodeValue();
                String sPower = nAttr.getNamedItem("power").getNodeValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date d = sdf.parse(sDate);
                    float p = Float.parseFloat(sPower);
                    pd.mPowerVector.add(new XMLPowerDataCell(d, p));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            data.mPowerData = pd;
        } else {
        }
        return data;
    }
}
